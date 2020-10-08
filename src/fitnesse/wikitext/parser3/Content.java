package fitnesse.wikitext.parser3;

public class Content {
  public Content(String content) {
    this.content = content;
    this.current = 0;
    this.isStartLine = true;
  }

  public Content(Content other) {
    this.content = other.content;
    this.current = other.current;
    this.isStartLine = other.isStartLine;
  }

  public void setStartLine() { isStartLine = true; }

  public void advance(int length) {
    isStartLine = false;
    current += length;
  }

  public char advance() {
    isStartLine = false;
    return content.charAt(current++);
  }

  public boolean startsWith(String match) {
    return content.startsWith(match, current);
  }

  public boolean more() {
    return current < content.length();
  }

  public boolean isBlankSpace() {
    if (!more()) return false;
    char candidate = content.charAt(current);
    return Character.isWhitespace(candidate) && candidate != '\n' && candidate != '\r';
  }

  public boolean isStartLine() {
    return isStartLine;
  }

  private String content;
  private int current;
  private boolean isStartLine;
}
