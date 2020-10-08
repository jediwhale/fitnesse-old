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
    return new Parser(input, tokenTypes, rules).parseToEnd();
  }

  Parser noWikiLinks() {
    return new Parser(this.tokens, this.rules, content -> false, this.watchTokens);
  }

  Parser watchTokens(Consumer<Token> watchTokens) {
    return new Parser(this.tokens, this.rules, this.isWikiLink, watchTokens);
  }

  Parser withContent(String content) {
    return new Parser(new TokenSource(tokens, content), rules, isWikiLink, token -> {});
  }

  Parser(String input, List<TokenType> tokenTypes, Map<TokenType, ParseRule> rules) {
    this.tokens = new TokenSource(input, tokenTypes);
    this.rules = rules;
    isWikiLink = WikiPath::isWikiWordPath; //todo: could be a rule?
    this.watchTokens = token -> {};
  }

  private Parser(TokenSource tokens, Map<TokenType, ParseRule> rules, Predicate<String> isWikiLink, Consumer<Token> watchTokens) {
    this.tokens = tokens;
    this.rules = rules;
    this.watchTokens = watchTokens;
    this.isWikiLink = isWikiLink;
  }

  Symbol parse(String input) {
    return parse(input, rules);
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
    Parser child = watchTokens(token -> result.append(token.getContent()));
    child.parseToTerminator(start.terminator(), child::parseCurrent, result::append);
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
  private final Consumer<Token> watchTokens;
}
