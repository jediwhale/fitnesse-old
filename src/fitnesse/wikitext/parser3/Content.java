package fitnesse.wikitext.parser3;

public class Content {
  public Content(String content) {
    this(content, 0);
  }

  public Content(Content other) {
    this(other.content, other.current);
  }

  public boolean startsWith(String match) {
    return content.startsWith(match, current);
  }

  public void advance(int length) { current += length; }

  public char advance() {
    return content.charAt(current++);
  }

  public boolean more() {
    return current < content.length();
  }

  public boolean isBlankSpace() {
    return current  < content.length() && Character.isWhitespace(content.charAt(current));
  }

  public boolean isStartLine() {
    return current == 0 || content.charAt(current - 1) == '\n' || content.charAt(current - 1) == '\r';
  }

  private Content(String content, int current) {
    this.content = content;
    this.current = current;
  }

  private final String content;
  private int current;
}
