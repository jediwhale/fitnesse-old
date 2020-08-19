package fitnesse.wikitext.parser3;

public class Token {
  public Token(TokenType type, String content) {
    this.type = type;
    this.content = content;
  }

  public Token(TokenType type) {
    this(type, "");
  }

  public TokenType getType() { return type; }
  public String getContent() { return content; }

  public boolean isType(TokenType type) { return this.type == type; }

  public boolean isStartType() {
    return isType(TokenType.BRACE_START) || isType(TokenType.BRACKET_START) || isType(TokenType.PARENTHESIS_START);
  }

  public Terminator terminator() {
    return new Terminator(endType());// todo: can inline endType() and use static terminators
  }

  private TokenType endType() {
    return isType(TokenType.BRACE_START) ? TokenType.BRACE_END
      : isType(TokenType.BRACKET_START) ? TokenType.BRACKET_END
      : isType(TokenType.PARENTHESIS_START) ? TokenType.PARENTHESIS_END
      : isType(TokenType.PREFORMAT_START) ? TokenType.PREFORMAT_END
      : isType(TokenType.ALIAS_START) ? TokenType.ALIAS_MIDDLE
      : isType(TokenType.ALIAS_MIDDLE) ? TokenType.ALIAS_END
      : type;
  }

  public Symbol asSymbol(SymbolType type) { return new Symbol(type, content); }

  public boolean isEndOfLine() {
    return isType(TokenType.NEW_LINE) || isType(TokenType.END);
  }

  public boolean isVariable() {
    return isType(TokenType.TEXT) && content.chars().allMatch(c -> Character.isLetterOrDigit(c) || c == '_');
  }

  public boolean isWord() {
    return isType(TokenType.TEXT) && content.chars().allMatch(c -> Character.isLetterOrDigit(c) || c == '_' || c == '.');
  }

  public String toString() {
    return type.toString() + (content.length() > 0 ? "=" + content : "");
  }

  private final TokenType type;
  private final String content;
}
