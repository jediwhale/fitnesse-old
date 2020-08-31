package fitnesse.wikitext.parser3;

public class Content {
  public Content(String content) {
    this.content = content;
  }

  public boolean startsWith(String match) {
    return startsWith(match, 0);
  }

  public boolean startsWith(String match, int offset) {
    return content.startsWith(match, current + offset);
  }

  public void advance(int length) { current += length; }

  public char advance() {
    return content.charAt(current++);
  }

  public boolean more() {
    return current < content.length();
  }

  public boolean isBlankSpaceAt(int offset) {
    return current + offset < content.length() && Character.isWhitespace(content.charAt(current + offset));
  }

  private final String content;
  private int current = 0;
}
