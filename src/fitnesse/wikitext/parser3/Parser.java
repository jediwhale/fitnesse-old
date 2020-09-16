package fitnesse.wikitext.parser3;

import fitnesse.wikitext.VariableStore;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class Parser {
  public static Symbol parse(String input, VariableStore variables) {
    return parse(input, variables, new Scanner());
  }

  public static Symbol parse(String input, VariableStore variables, Scanner scanner) {
    return new Parser(scanner.scan(input), variables).parseList(END_TERMINATOR, error -> error);
  }

  public Parser(TokenList tokens, VariableStore variables) {
    this.tokens = tokens;
    this.variables = variables;
    isWikiLink = WikiPath::isWikiWordPath;
  }

  public Parser(Parser parent) {
    this.tokens = parent.tokens;
    this.variables = parent.variables;
    isWikiLink = content -> false;
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
    return peek(0).getType().parse(this);
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
    return variables.findVariable(name);
  }

  public void putVariable(String name, String value) {
    variables.putVariable(name, value);
  }

  private Symbol parseList(Terminator terminator, Function<String, String> message) {
    Symbol result = new Symbol(SymbolType.LIST);
    tokens.consumeToTerminator(terminator,
      token -> parseToken(result, token),
      error -> result.addErrorFirst(message.apply(error)));
    advance();
    return result;
  }

  public static Symbol defaultRule(Parser parser) {
    String content = parser.peek(0).getContent();
    Symbol result = new Symbol(parser.isWikiLink.test(content) ? SymbolType.WIKI_LINK : SymbolType.TEXT, content);
    parser.advance();
    return result;
  }

  private void appendContent(StringBuilder builder, Token token) {
    builder.append(token.getContent());
    advance();
  }

  private void parseToken(Symbol parent, Token token) {
    if (token.isType(TokenType.NESTING_START)) {
      parseNesting(parent);
    } else {
      parent.add(parseCurrent());
    }
  }

  private void parseNesting(Symbol parent) {
    advance();
    parseToTerminator(parent, NESTING_TERMINATOR);
    advance();
  }

  private static final Terminator END_TERMINATOR = new Terminator(TokenType.END);
  private static final Terminator NESTING_TERMINATOR = new Terminator(TokenType.NESTING_END);

  private final Predicate<String> isWikiLink;
  private final TokenList tokens;
  private final VariableStore variables;
}
