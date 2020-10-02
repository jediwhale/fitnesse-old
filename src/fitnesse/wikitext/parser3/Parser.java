package fitnesse.wikitext.parser3;

import fitnesse.wikitext.VariableStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

class Parser {
  static Symbol parse(String input, VariableStore variables) {
    return parse(input, variables, TokenTypes.WIKI_PAGE_TYPES);
  }

  static Symbol parse(String input, VariableStore variables, List<TokenType> tokenTypes) {
    return new Parser(new Content(input), tokenTypes, variables)
      .parseList(SymbolType.LIST, END_TERMINATOR, (list, error) -> {});
  }

  Parser(Content content, List<TokenType> tokenTypes, VariableStore variables) {
    this.tokens = new TokenSource(content, tokenTypes);
    this.variables = variables;
    isWikiLink = WikiPath::isWikiWordPath;
  }

  Parser(Parser parent) {
    this.tokens = parent.tokens;
    this.variables = parent.variables;
    isWikiLink = content -> false;
  }

  Token peek(int offset) { return tokens.peek(offset); }
  void putBack() { tokens.putBack(); }
  void putInput(String input) { tokens.putContent(input); }
  Token advance() { return tokens.take(); }

  Symbol parseCurrent() {
    return peek(0).getType().parse(this);
  }

  Symbol makeError(String message, int tokenCount) {
    StringBuilder contents = new StringBuilder();
    for (int i = 0; i < tokenCount; i++) {
      contents.append(peek(0).getContent());
      advance();
    }
    return Symbol.error(contents + " " + message);
  }

  Optional<String> getVariable(String name) {
    return variables.findVariable(name);
  }

  void putVariable(String name, String value) {
    variables.putVariable(name, value);
  }

  Symbol parseList(Token start) {
    return parseList(SymbolType.LIST, start, "");
  }

  Symbol parseList(SymbolType symbolType, Token start) {
    return parseList(symbolType, start, "");
  }

  Symbol parseList(SymbolType symbolType, Token start, String errorPrefix) {
    return parseList(symbolType, start.terminator(),
      (list, error) -> list.add(0, Symbol.error(errorPrefix + start.getContent() + " " + error)));
  }

  String parseText(Token start) {
    StringBuilder result = new StringBuilder();
    parseToTerminator(start.terminator(),
      () -> appendCurrent(result),
      result::append);
    advance();
    return result.toString();
  }

  static Symbol defaultRule(Parser parser) {
    String content = parser.peek(0).getContent();
    Symbol result = new Symbol(parser.isWikiLink.test(content) ? SymbolType.WIKI_LINK : SymbolType.TEXT, content);
    parser.advance();
    return result;
  }

  private Symbol parseList(SymbolType symbolType, Terminator terminator, BiConsumer<List<Symbol>, String> onError) {
    List<Symbol> symbols = new ArrayList<>();
    parseToTerminator(terminator,
      () -> symbols.add(parseCurrent()),
      error -> onError.accept(symbols, error));
    advance();
    return Symbol.make(symbolType, symbols);
  }

  private void appendCurrent(StringBuilder builder) {
    builder.append(peek(0).getContent());
    advance();
  }

  private void parseToTerminator(Terminator terminator, Runnable action, Consumer<String> onError) {
    while (true) {
      Token token = peek(0);
      if (terminator.matches(token.getType())) break;
      if (token.isType(TokenType.END)) {
        onError.accept("Missing terminator: " + terminator.getName());
        break;
      }
      action.run();
    }
  }

  private static final Terminator END_TERMINATOR = new Terminator(TokenType.END);

  private final Predicate<String> isWikiLink;
  private final TokenSource tokens;
  private final VariableStore variables;
}
