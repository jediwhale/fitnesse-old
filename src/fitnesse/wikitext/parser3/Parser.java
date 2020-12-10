package fitnesse.wikitext.parser3;

import fitnesse.wikitext.ParsingPage;
import fitnesse.wikitext.VariableSource;
import fitnesse.wikitext.VariableStore;
import fitnesse.wikitext.parser.TextMaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.regex.Pattern;

class Parser {
  static Symbol parse(String input, VariableStore variables, External external) {
    return new Parser(input, TokenTypes.WIKI_PAGE_TYPES, variables, external).parseToEnd();
  }

  static Symbol parse(String input, ParsingPage page) {
    return parse(input, TokenTypes.WIKI_PAGE_TYPES, page);
  }

  static Symbol parse(String input, List<TokenType> tokenTypes, ParsingPage page) {
    return new Parser(input, tokenTypes, page, new ExternalAdapter(page.getPage())).parseToEnd();
  }

  Parser textType(SymbolType type) {
    return new Parser(tokens, variables, rules, (c, o) -> makeLeaf(type, c, o), this.watchTokens, parentTerminator);
  }

  Parser watchTokens(Consumer<Token> watchTokens) {
    return new Parser(tokens, variables, rules, makeSymbolFromText, watchTokens, parentTerminator);
  }

  Parser withContent(String content) {
    return new Parser(new TokenSource(tokens, new Content(content, this::substituteVariable)), variables, rules, makeSymbolFromText, token -> {}, parentTerminator);
  }

  Parser withTerminator(Terminator parentTerminator) {
    return new Parser(tokens, variables, rules, makeSymbolFromText, watchTokens, parentTerminator);
  }

  Parser withTokenTypes(List<TokenType> tokenTypes) {
    return new Parser(new TokenSource(tokens, tokenTypes), variables, rules, makeSymbolFromText, watchTokens, parentTerminator);
  }

  Parser(String input, List<TokenType> tokenTypes, VariableStore variables, External external) {
    this.tokens = new TokenSource(new Content(input, this::substituteVariable), tokenTypes);
    this.rules = ParseRules.make(variables, external);
    this.makeSymbolFromText = Parser::determineTextSymbol;
    this.watchTokens = token -> {};
    this.variables = variables;
    this.parentTerminator = Terminator.NONE;
  }

  private Parser(TokenSource tokens, VariableSource variables,  Map<TokenType, ParseRule> rules, BiFunction<String, Integer, Symbol> makeSymbolFromText, Consumer<Token> watchTokens, Terminator parentTerminator) {
    this.tokens = tokens;
    this.rules = rules;
    this.watchTokens = watchTokens;
    this.makeSymbolFromText = makeSymbolFromText;
    this.variables = variables;
    this.parentTerminator = parentTerminator;
  }

  //todo: public surface is getting bigger and bigger...

  Token peek(int offset) { return offset >= 0 ? tokens.peek(offset) : tokens.getPrevious(); }
  void putBack() { tokens.putBack(); }
  void pushTypes(List<TokenType> types) { tokens.use(types, type -> false); }
  void popTypes() { tokens.popTypes(); }

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

  String parseText(Terminator terminator) {
    StringBuilder result = new StringBuilder();
    Parser child = watchTokens(token -> result.append(token.getContent())).withTerminator(terminator);
    child.parseToTerminator(terminator, child::parseCurrent, result::append); //todo: test what error looks like?
    advance();
    return result.toString();
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

  private ContentSegment substituteVariable(String name) {
    return new ContentSegment(
        variables.findVariable(name)
          // the variable value is delimited with pseudo-nesting characters
          // these are used for compatibility with parser v2 to handle variables inside table cells
          .map(s -> Nesting.START + s + Nesting.END)
          .orElse(" !style_fail{Undefined variable: " + name + "} "));
  }

  private static Symbol defaultRule(Parser parser) {
    String content = parser.peek(0).getContent();
    Symbol result = parser.makeSymbolFromText.apply(content, parser.peek(0).getOffset());
    parser.advance();
    return result;
  }

  private static Symbol determineTextSymbol(String text, Integer offset) {
    int wikiWordLength = TextMaker.findWikiWordLength(text);
    SymbolType type =
      wikiWordLength > 0 ? SymbolType.WIKI_LINK
        : isEMail(text) ? SymbolType.EMAIL
        : SymbolType.TEXT;
    if (wikiWordLength == 0 || wikiWordLength == text.length()) {
      return makeLeaf(type, text, offset);
    }
    Symbol result = new BranchSymbol(SymbolType.LIST);
    result.add(makeLeaf(SymbolType.WIKI_LINK, text.substring(0, wikiWordLength), offset));
    result.add(determineTextSymbol(text.substring(wikiWordLength), offset + wikiWordLength));
    return result;
  }

  private static Symbol makeLeaf(SymbolType type, String text, Integer offset) {
    return new LeafSymbol(type, text, offset);
  }

  private static boolean isEMail(String text) {
    return text.indexOf("@") > 0 && Pattern.matches(eMailPattern, text);
  }

  private static final Terminator END_TERMINATOR = new Terminator(TokenType.END);
  private static final String eMailPattern = "[\\w-_.]+@[\\w-_.]+\\.[\\w-_.]+";

  //todo: too much stuff...
  private final BiFunction<String, Integer, Symbol> makeSymbolFromText;
  private final TokenSource tokens;
  private final Map<TokenType, ParseRule> rules;
  private final Consumer<Token> watchTokens;
  private final VariableSource variables;
  private final Terminator parentTerminator;
}
