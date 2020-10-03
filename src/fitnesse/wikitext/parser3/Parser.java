package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

class Parser {
  static Symbol parse(String input, Map<TokenType, ParseRule> rules) {
    return parse(input, TokenTypes.WIKI_PAGE_TYPES, rules);
  }

  static Symbol parse(String input, List<TokenType> tokenTypes, Map<TokenType, ParseRule> rules) {
    return new Parser(new Content(input), tokenTypes, rules)
      .parseList(SymbolType.LIST, END_TERMINATOR, (list, error) -> {});
  }

  Parser(Content content, List<TokenType> tokenTypes, Map<TokenType, ParseRule> rules) {
    this.tokens = new TokenSource(content, tokenTypes);
    this.rules = rules;
    isWikiLink = WikiPath::isWikiWordPath;
  }

  Parser(Parser parent) {
    this.tokens = parent.tokens;
    this.rules = parent.rules;
    isWikiLink = content -> false;
  }

  Symbol parse(String input) {
    return parse(input, rules);
  }

  Token peek(int offset) { return tokens.peek(offset); }
  void putBack() { tokens.putBack(); }
  void putInput(String input) { tokens.putContent(input); }
  Token advance() { return tokens.take(); }

  Symbol parseCurrent() {
    return rules.getOrDefault(peek(0).getType(), Parser::defaultRule).parse(this);
  }

  Symbol makeError(String message, int tokenCount) {
    StringBuilder contents = new StringBuilder();
    for (int i = 0; i < tokenCount; i++) {
      contents.append(peek(0).getContent());
      advance();
    }
    return Symbol.error(contents + " " + message);
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

  private static Symbol defaultRule(Parser parser) {
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
  private final Map<TokenType, ParseRule> rules;
}
