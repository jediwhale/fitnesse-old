package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.VariableSource;

import java.util.Stack;

class Content {
  Content(String content, VariableSource variables) {
    this.variables = variables;
    insert(new ContentSegment(content));
    isStartLine = true;
  }

  Content(Content other) {
    this.variables = other.variables;
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
    insert(substituteVariable(name));
  }

  void setStartLine() { isStartLine = true; }
  boolean isStartLine() { return isStartLine; }

  private void insert(ContentSegment segment) {
    if (!segment.atEnd(0)) items.push(segment);
  }

  private ContentSegment substituteVariable(String name) {
    return new ContentSegment(
      variables.findVariable(name)
        // the variable value is delimited with pseudo-nesting characters
        // these are used for compatibility with parser v2 to handle variables inside table cells
        .map(s -> Nesting.START + s + Nesting.END)
        .orElse(" !style_fail{Undefined variable: " + name + "} "));
  }


  private final Stack<ContentSegment> items = new Stack<>();
  private final VariableSource variables;
  private boolean isStartLine;
}

