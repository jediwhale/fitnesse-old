package fitnesse.wikitext.parser3;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class Parser {
  public static Symbol parse(String input, VariableSource variables) {
    return new Parser(new Scanner(input).scan(), variables).parseList(END_TERMINATOR, error -> error);
  }

  public Parser(TokenList tokens, VariableSource variables) {
    this.tokens = tokens;
    this.variables = variables;
  }

  public Symbol parseString(String input) {
    return parse(input, variables);
  }

  public Token peek(int offset) {
    return tokens.peek(offset);
  }

  public void move(int movement) {
    tokens.move(movement);
  }

  public void advance() {
    move(1);
  }

  public void parseToTerminator(Symbol parent, Terminator terminator) {
    tokens.consumeToTerminator(terminator,
      token -> parseToken(parent, token),
      parent::addError);
  }

  public Symbol parseList(Token start) {
    return parseList(start, "");
  }

  public Symbol parseList(Token start, String errorPrefix) {
    return parseList(start.terminator(), error -> errorPrefix + start.getContent() + " " + error);
  }

  public String parseText(Token start) {
    StringBuilder result = new StringBuilder();
    tokens.consumeToTerminator(start.terminator(),
      token -> appendContent(result, token),
      result::append);
    advance();
    return result.toString();
  }

  public Symbol parseCurrent() {
    TokenType type = peek(0).getType();
    return parseRules.containsKey(type)
      ? parseRules.get(type).parse(this)
      : defaultRule();
  }

  public Symbol makeError(String message, int tokenCount) {
    StringBuilder contents = new StringBuilder();
    for (int i = 0; i < tokenCount; i++) {
      contents.append(peek(0).getContent());
      advance();
    }
    return Symbol.error(contents + " " + message);
  }

  public Optional<String> getVariable(String name) {
    return variables.get(name);
  }

  public void putVariable(String name, String value) {
    variables.put(name, value);
  }

  private Symbol parseList(Terminator terminator, Function<String, String> message) {
    Symbol result = new Symbol(SymbolType.LIST);
    tokens.consumeToTerminator(terminator,
      token -> parseToken(result, token),
      error -> result.addErrorFirst(message.apply(error)));
    advance();
    return result;
  }

  private Symbol defaultRule() {
    Symbol result = peek(0).asSymbol(SymbolType.TEXT); //todo: or could be 'unexpected token' error?
    advance();
    return result;
  }

  private void appendContent(StringBuilder builder, Token token) {
    builder.append(token.getContent());
    advance();
  }

  private void parseToken(Symbol parent, Token token) {
    if (token.isType(TokenType.LITERAL_START)) {
      parseLiteral(parent);
    } else if (token.isType(TokenType.NESTING_START)) {
      parseNesting(parent);
    } else {
      parent.add(parseCurrent());
    }
  }

  private void parseLiteral(Symbol parent) {
    advance();
    tokens.consumeToTerminator(LITERAL_TERMINATOR,
      token -> addText(parent, token),
      parent::addError);
    advance();
  }

  private void addText(Symbol parent, Token token) {
    parent.add(token.asSymbol(SymbolType.TEXT));
    advance();
  }

  private void parseNesting(Symbol parent) {
    advance();
    parseToTerminator(parent, NESTING_TERMINATOR);
    advance();
  }

  private static final Terminator END_TERMINATOR = new Terminator(TokenType.END);
  private static final Terminator LITERAL_TERMINATOR = new Terminator(TokenType.LITERAL_END);
  private static final Terminator NESTING_TERMINATOR = new Terminator(TokenType.NESTING_END);

  private static final HashMap<TokenType, ParseRule> parseRules = new HashMap<>();

  static {
    parseRules.put(TokenType.ALIAS_START, Alias::parse);
    parseRules.put(TokenType.ANCHOR_NAME, Keyword.parseWord(SymbolType.ANCHOR_NAME));
    parseRules.put(TokenType.ANCHOR_REFERENCE, Keyword.parseWord(SymbolType.ANCHOR_REFERENCE));
    parseRules.put(TokenType.BOLD, Pair.parse(SymbolType.BOLD));
    parseRules.put(TokenType.BOLD_ITALIC, Pair.parse(SymbolType.BOLD_ITALIC));
    parseRules.put(TokenType.DEFINE, Variable::parsePut);
    parseRules.put(TokenType.ITALIC, Pair.parse(SymbolType.ITALIC));
    parseRules.put(TokenType.LINK, Link::parse);
    parseRules.put(TokenType.PATH, Keyword.parse(SymbolType.PATH));
    parseRules.put(TokenType.PREFORMAT_START, Pair.parse(SymbolType.PREFORMAT));
    parseRules.put(TokenType.SEE, Keyword.parse(SymbolType.SEE));
    parseRules.put(TokenType.STRIKE, Pair.parse(SymbolType.STRIKE));
    parseRules.put(TokenType.STYLE, Style::parse);
    parseRules.put(TokenType.VARIABLE, Variable::parseGet);
    parseRules.put(TokenType.WIKI_PATH, WikiPath::parse);
  }

  private final TokenList tokens;
  private final VariableSource variables;
}
