package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

class Parser {
  static Symbol parse(String input, Map<TokenType, ParseRule> rules) {
    return parse(input, TokenTypes.WIKI_PAGE_TYPES, rules);
  }

  static Symbol parse(String input, List<TokenType> tokenTypes, Map<TokenType, ParseRule> rules) {
    return new Parser(input, tokenTypes, rules).parseToEnd();
  }

  Parser textType(SymbolType type) {
    return new Parser(this.tokens, this.rules, content -> type, this.watchTokens);
  }

  Parser watchTokens(Consumer<Token> watchTokens) {
    return new Parser(this.tokens, this.rules, this.textType, watchTokens);
  }

  Parser withContent(String content) {
    return new Parser(new TokenSource(tokens, content), rules, textType, token -> {});
  }

  Parser(String input, List<TokenType> tokenTypes, Map<TokenType, ParseRule> rules) {
    this.tokens = new TokenSource(input, tokenTypes);
    this.rules = rules;
    textType = text -> WikiPath.isWikiWordPath(text) ? SymbolType.WIKI_LINK : SymbolType.TEXT;
    this.watchTokens = token -> {};
  }

  private Parser(TokenSource tokens, Map<TokenType, ParseRule> rules, Function<String, SymbolType> textType, Consumer<Token> watchTokens) {
    this.tokens = tokens;
    this.rules = rules;
    this.watchTokens = watchTokens;
    this.textType = textType;
  }

  Token peek(int offset) { return offset >= 0 ? tokens.peek(offset) : tokens.getPrevious(); }
  void putBack() { tokens.putBack(); }

  Token advance() {
    Token result =  tokens.take();
    watchTokens.accept(result);
    return result;
  }

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

  Symbol parseToEnd() {
    return parseList(SymbolType.LIST, END_TERMINATOR, (list, error) -> {});
  }

  Symbol parseList(Token start) {
    return parseList(SymbolType.LIST, start);
  }

  Symbol parseList(SymbolType symbolType, Token start) {
    return parseList(symbolType, Terminator.make(start));
  }

  Symbol parseList(SymbolType symbolType, Terminator terminator) {
    return parseList(symbolType, terminator,
      (list, error) -> list.add(0, Symbol.error(error)));
  }

  String parseText(Terminator terminator) {
    StringBuilder result = new StringBuilder();
    Parser child = watchTokens(token -> result.append(token.getContent()));
    child.parseToTerminator(terminator, child::parseCurrent, result::append); //todo: test what error looks like?
    advance();
    return result.toString();
  }

  private Symbol parseList(SymbolType symbolType, Terminator terminator, BiConsumer<List<Symbol>, String> onError) {
    List<Symbol> symbols = new ArrayList<>();
    parseToTerminator(terminator,
      () -> symbols.add(parseCurrent()),
      error -> onError.accept(symbols, error));
    advance();
    return Symbol.make(symbolType, symbols);
  }

  private void parseToTerminator(Terminator terminator, Runnable action, Consumer<String> onError) {
    while (true) {
      Token token = peek(0);
      if (terminator.matches(token.getType())) break;
      if (token.isType(TokenType.END)) {
        onError.accept(terminator.missing());
        break;
      }
      action.run();
    }
  }

  private static Symbol defaultRule(Parser parser) {
    String content = parser.peek(0).getContent();
    Symbol result = new LeafSymbol(parser.textType.apply(content), content);
    parser.advance();
    return result;
  }

  private static final Terminator END_TERMINATOR = new Terminator(TokenType.END);

  private final Function<String, SymbolType> textType;
  private final TokenSource tokens;
  private final Map<TokenType, ParseRule> rules;
  private final Consumer<Token> watchTokens;
}
