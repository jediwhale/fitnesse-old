package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.ParsingPage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

class Parser {

  static Symbol parse(String input, ParsingPage page) {
    return new Parser(input, TokenTypes.WIKI_PAGE_TYPES, page).parseToEnd();
  }

  static Symbol parse(String input, TokenTypes tokenTypes, ParsingPage page) {
    return new Parser(input, tokenTypes, page).parseToEnd();
  }

  Parser textType(SymbolType type) {
    return new Parser(tokens, page, rules.withTextType(type), parentTerminator);
  }

  Parser withContent(String content) {
    return new Parser(new TokenSource(tokens, new Content(content, page)), page, rules, parentTerminator);
  }

  Parser withTerminator(Terminator parentTerminator) {
    return new Parser(tokens, page, rules, parentTerminator);
  }

  Parser withTokenTypes(TokenTypes tokenTypes) {
    return new Parser(new TokenSource(tokens, tokenTypes), page, rules, parentTerminator);
  }

  Parser(String input, TokenTypes tokenTypes, ParsingPage page) {
    this.page = page;
    this.tokens = new TokenSource(new Content(input, page), tokenTypes);
    this.rules = new ParseRules(page);
    this.parentTerminator = Terminator.NONE;
  }

  private Parser(TokenSource tokens, ParsingPage page, ParseRules rules, Terminator parentTerminator) {
    this.tokens = tokens;
    this.rules = rules;
    this.page = page;
    this.parentTerminator = parentTerminator;
  }

  //todo: public surface is getting bigger and bigger...

  Token peek(int offset) { return offset >= 0 ? tokens.peek(offset) : tokens.getPrevious(); }
  void putBack() { tokens.putBack(); }
  void pushTypes(TokenTypes types) { tokens.use(types, type -> false); }
  void popTypes() { tokens.popTypes(); }

  Token advance() {
    return tokens.take();
  }

  Symbol parseCurrent() {
    return rules.parse(this);
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
    return parseList(SymbolType.LIST, END_TERMINATOR, Terminator.NONE, this);
  }

  Symbol parseList(Token start) {
    return parseList(SymbolType.LIST, start);
  }

  Symbol parseList(SymbolType symbolType, Token start) {
    return parseList(symbolType, Terminator.make(start));
  }

  Symbol parseList(SymbolType symbolType, Terminator terminator) {
    return parseList(symbolType, terminator, Terminator.NONE, withTerminator(terminator));
  }

  Symbol parseListInParent(SymbolType symbolType, Terminator terminator) {
    return parseList(symbolType, terminator, parentTerminator, withTerminator(parentTerminator));
  }

  <R> R collectText(Terminator terminator, Function<String, R> mapResult, BiFunction<String, String, R> mapError) {
    StringBuilder text = new StringBuilder();
    while (true) {
      Token next = advance();
      if (terminator.matches(next.getType())) break;
      if (next.isType(TokenType.END)) {
        return mapError.apply(text.toString(), terminator.missing("!define"));
      }
      text.append(next.getContent());
    }
    return mapResult.apply(text.toString());
  }

  private Symbol parseList(SymbolType symbolType, Terminator includedTerminator, Terminator excludedTerminator, Parser child) {
    List<Symbol> symbols = new ArrayList<>();
    parseToTerminator(Terminator.make(includedTerminator, excludedTerminator),
      () -> symbols.add(child.parseCurrent()),
      error ->symbols.add(0, Symbol.error(error)));
    if (includedTerminator.matches(peek(0).getType())) advance();
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

  private static final Terminator END_TERMINATOR = new Terminator(TokenType.END);

  //todo: too much stuff?
  private final ParsingPage page;
  private final TokenSource tokens;
  private final ParseRules rules;
  private final Terminator parentTerminator;
}
