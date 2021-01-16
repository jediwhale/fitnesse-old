package fitnesse.wikitext.parser3;

public class Token {
  public Token(TokenType type, String content) {
    this.type = type;
    this.content = content;
    this.offset = -1;
  }

  public Token(TokenType type) {
    this(type, "");
  }

  public Token(String content, int offset) {
    this(TokenType.TEXT, content, offset);
  }

  public Token(TokenType type, String content, int offset) {
    this.type = type;
    this.content = content;
    this.offset = offset;
  }

  public TokenType getType() { return type; }
  public String getContent() { return content; }
  public int getOffset() { return offset; }

  public boolean isType(TokenType type) { return this.type == type; }

  public boolean isStartType() {
    return isType(DelimiterType.BRACE_START) || isType(DelimiterType.BRACKET_START) || isType(DelimiterType.PARENTHESIS_START);
  }

  public boolean isEndOfLine() {
    return isType(DelimiterType.NEW_LINE) || isType(TokenType.END) || isType(DelimiterType.NESTING_END);
  }

  public boolean isWord() {
    return isType(TokenType.TEXT) && content.chars().allMatch(c -> Character.isLetterOrDigit(c) || c == '_' || c == '.');
  }

  public String toString() {
    return type.toString() + (content.length() > 0 ? "=" + content : "");
  }

  private final TokenType type;
  private final String content;
  private final int offset;

}
