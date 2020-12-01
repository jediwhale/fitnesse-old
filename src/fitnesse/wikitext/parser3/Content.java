package fitnesse.wikitext.parser3;

import java.util.Stack;
import java.util.function.Function;

class Content {
  Content(String content, Function<String, ContentSegment> substituteVariable) {
    this.substituteVariable = substituteVariable;
    insert(new ContentSegment(content));
    isStartLine = true;
  }

  Content(Content other) {
    this.substituteVariable = other.substituteVariable;
    this.isStartLine = other.isStartLine;
    for (int i = 0; i < other.items.size(); i++) items.push(new ContentSegment(other.items.elementAt(i)));
  }

  int getCurrent() { return items.size() == 1 ? items.peek().getCurrent() : -1; }

  boolean startsWith(String match) {
    if (!more()) return false;
    int item = items.size() - 1;
    int offset = -1;
    for (int i = 0; i < match.length(); i++) {
      offset++;
      if (items.elementAt(item).atEnd(offset)) {
        if (item <= 0) return false;
        item--;
        offset = 0;
      }
      if (!items.elementAt(item).isCharAt(match.charAt(i), offset)) return false;
    }
    return true;
  }

  boolean isBlankSpace() {
    return more() && items.peek().test(candidate -> Character.isWhitespace(candidate) && candidate != '\n' && candidate != '\r');
  }

  boolean isDigit() {
    return more() && items.peek().test(Character::isDigit);
  }

  boolean more() { return !items.empty(); }

  char advance() {
    isStartLine = false;
    char result = items.peek().advance();
    if (items.peek().atEnd(0)) items.pop();
    return result;
  }

  void advance(int length) {
    for (int i = 0; i < length; i++) advance();
  }

  void insertVariable(String name) {
    insert(substituteVariable.apply(name));
  }

  void insert(ContentSegment segment) {
    if (!segment.atEnd(0)) items.push(segment);
  }

  void setStartLine() { isStartLine = true; }
  boolean isStartLine() { return isStartLine; }

  private final Stack<ContentSegment> items = new Stack<>();
  private final Function<String, ContentSegment> substituteVariable;
  private boolean isStartLine;
}

