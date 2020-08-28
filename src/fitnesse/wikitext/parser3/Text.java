package fitnesse.wikitext.parser3;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Text {

  public Text(String text) {
    this(text, 0);
  }

  public Text(String text, int offset) {
    this.text = text;
    this.offset = offset;
    start = offset;
    end = text.length();
  }

  public boolean equals(String other) {
    return other.length() == end - start && text.startsWith(other, start);
  }

  public boolean at(int offset, Predicate<Character> matcher) {
    return matcher.test(text.charAt(start + offset));
  }

  public String content() {
    return text.substring(start, end);
  }

  public int length() {
    return end - start;
  }

  public void applyAll(String separator, Consumer<Text> applier) {
    matchAll(separator, text -> {
      applier.accept(text);
      return true;
    });
  }

  public boolean matchAll(String separator, Predicate<Text> matcher) {
    start = offset;
    while (start < text.length()) {
      end = text.indexOf(separator, start);
      if (end == -1) end = text.length();
      if (!matcher.test(this)) return false;
      if (end < text.length()) end++;
      start = end;
    }
    return true;
  }


  private final String text;
  private final int offset;
  private int start;
  private int end;
}
