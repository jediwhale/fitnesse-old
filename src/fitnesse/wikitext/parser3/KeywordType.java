package fitnesse.wikitext.parser3;

import java.util.Arrays;
import java.util.List;

class KeywordType extends TokenType {

  static final KeywordType ANCHOR_NAME = new KeywordType("AnchorName", "!anchor");
  static final KeywordType CENTER = new KeywordType("Center", "!c"); //todo: was atStart?
  static final KeywordType CONTENTS = new KeywordType("Contents", "!contents");
  static final KeywordType DEFINE = new KeywordType("Define", "!define");
  static final KeywordType HEADER = new KeywordType("Header", "!1", "!2", "!3", "!4", "!5", "!6"); //todo: was atStart?
  static final KeywordType HEADINGS = new KeywordType("Headings", "!headings"); //todo: was atStart?
  static final KeywordType HELP = new KeywordType("Help", "!help");
  static final KeywordType IMAGE = new KeywordType("Image", "!img", "!img-l", "!img-r");
  static final KeywordType INCLUDE = new KeywordType("Include", "!include");
  static final KeywordType LAST_MODIFIED = new KeywordType("LastModified", "!lastmodified");
  static final KeywordType META = new KeywordType("Meta", "!meta");
  static final KeywordType NOTE = new KeywordType("Note", "!note");
  static final KeywordType PATH = new KeywordType("Path", "!path"); //todo: was atStart?
  static final KeywordType SEE = new KeywordType("See", "!see");
  static final KeywordType STYLE = new KeywordType("Style", "!style");
  static final KeywordType TODAY = new KeywordType("Today", "!today");

  KeywordType(String name, String ... matches)  {
    super(name, matches[0]);
    this.matches = Arrays.asList(matches);
  }

  boolean isMatch(String text) {
    return matches.contains(text);
  }

  private final List<String> matches;
}
