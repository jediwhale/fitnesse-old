package fitnesse.wikitext.parser3;

import java.util.function.Predicate;

class ContentSegment {
  ContentSegment(String content, boolean equalityMask) {
    this.content = content;
    this.equalityMask = equalityMask;
    current = 0;
  }

  ContentSegment(ContentSegment other) {
    this.content = other.content;
    this.current = other.current;
    this.equalityMask = other.equalityMask;
  }

  boolean isCharAt(char match, int offset) {
    return content.charAt(current + offset) == match && equalityMask;
  }

  boolean test(Predicate<Character> predicate) {
    return predicate.test(content.charAt(current));
  }

  char advance() { return content.charAt(current++); }
  boolean atEnd(int offset) { return current + offset >= content.length(); }
  int getCurrent() { return current; }

  private final String content;
  private final boolean equalityMask;
  private int current;
}
