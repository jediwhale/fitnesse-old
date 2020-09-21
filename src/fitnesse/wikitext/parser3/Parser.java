package fitnesse.wikitext.parser3;

import fitnesse.wikitext.VariableStore;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Parser {
  public static Symbol parse(String input, VariableStore variables) {
    return parse(input, variables, TokenTypes.WIKI_PAGE_TYPES);
  }

  public static Symbol parse(String input, VariableStore variables, List<TokenType> tokenTypes) {
    return new Parser(new Content(input), tokenTypes, variables).parseList(END_TERMINATOR, error -> error);
  }

  public Parser(Content content, List<TokenType> tokenTypes, VariableStore variables) {
    this.content = content;
    this.tokens = new TokenSource(content, tokenTypes);
    this.variables = variables;
    isWikiLink = WikiPath::isWikiWordPath;
  }

  public Parser(Parser parent) {
    this.content = parent.content;
    this.tokens = parent.tokens;
    this.variables = parent.variables;
    isWikiLink = content -> false;
  }

  public Token peek(int offset) { return tokens.peek(offset); }

  public void putBack() { tokens.putBack(); }

  public void putInput(String input) {
    content.put(input);
  }

  public Token advance() { return tokens.take(); }

  public void parseToTerminator(Symbol parent, Terminator terminator) {
    consumeToTerminator(terminator,
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
    consumeToTerminator(start.terminator(),
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
    consumeToTerminator(terminator,
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

  private void consumeToTerminator(Terminator terminator, Consumer<Token> action, Consumer<String> error) {
    while (true) {
      Token token = peek(0);
      if (terminator.matches(token)) break;
      if (token.isType(TokenType.END)) {
        error.accept("Missing terminator: " + terminator.getName());
        break;
      }
      action.accept(token);
    }
  }

  private static final Terminator END_TERMINATOR = new Terminator(TokenType.END);
  private static final Terminator NESTING_TERMINATOR = new Terminator(TokenType.NESTING_END);

  private final Predicate<String> isWikiLink;
  private final Content content;
  private final TokenSource tokens;
  private final VariableStore variables;
}
