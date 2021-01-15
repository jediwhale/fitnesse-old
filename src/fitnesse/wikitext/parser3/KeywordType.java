package fitnesse.wikitext.parser3;

class KeywordType extends TokenType {

  static final KeywordType ANCHOR_NAME = new KeywordType("AnchorName", "!anchor");
  static final KeywordType CENTER = new KeywordType("Center", "!c"); //todo: was atStart?
  static final KeywordType INCLUDE = new KeywordType("Include", "!include");
  static final KeywordType LAST_MODIFIED = new KeywordType("LastModified", "!lastmodified");
  static final KeywordType META = new KeywordType("Meta", "!meta");
  static final KeywordType NOTE = new KeywordType("Note", "!note");
  static final KeywordType TODAY = new KeywordType("Today", "!today");

  KeywordType(String name, String match) {
    super(name, match);
  }
}
