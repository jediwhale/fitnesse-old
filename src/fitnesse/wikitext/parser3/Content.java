package fitnesse.wikitext.parser3;

public class Content {
  public Content(String content) {
    this.content = content;
  }

  public boolean readString(String match) {
    if (content.startsWith(match, current)) {
      current += match.length();
      return true;
    }
    return false;
  }

  public String readWord(String match) {
    if (content.startsWith(match, current) && isBlankSpaceAt(current + match.length())) {
      int start = current;
      current += match.length();
      readBlankSpace();
      return content.substring(start, current);
    }
    return "";
  }

  public String readBlankSpace() {
    int offset = 0;
    while (isBlankSpaceAt(current + offset)) {
      offset++;
    }
    current += offset;
    return content.substring(current - offset, current);
  }

  public char advance() {
    return content.charAt(current++);
  }

  public boolean more() {
    return current < content.length();
  }

  private boolean isBlankSpaceAt(int offset) {
    return offset < content.length() && Character.isWhitespace(content.charAt(offset));
  }

  private final String content;
  private int current = 0;
}
