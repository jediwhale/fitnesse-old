package fitnesse.wikitext.parser3;

class SymbolType {
  static final SymbolType ANCHOR_NAME = new SymbolType("ANCHOR_NAME");
  static final SymbolType ANCHOR_REFERENCE = new SymbolType("ANCHOR_REFERENCE");
  static final SymbolType BOLD = new SymbolType("BOLD");
  static final SymbolType BOLD_ITALIC = new SymbolType("BOLD_ITALIC");
  static final SymbolType CENTER = new SymbolType("CENTER");
  static final SymbolType COLLAPSIBLE = new SymbolType("COLLAPSIBLE");
  static final SymbolType CONTENTS = new SymbolType("CONTENTS");
  static final SymbolType DEFINE = new SymbolType("DEFINE");
  static final SymbolType EMAIL = new SymbolType("EMAIL");
  static final SymbolType ERROR = new SymbolType("ERROR");
  static final SymbolType EXPRESSION = new SymbolType("EXPRESSION");
  static final SymbolType HASH_TABLE = new SymbolType("HASH_TABLE");
  static final SymbolType HEADER = new SymbolType("HEADER");
  static final SymbolType HEADINGS = new SymbolType("HEADINGS");
  static final SymbolType HELP = new SymbolType("HELP");
  static final SymbolType IMAGE = new SymbolType("IMAGE");
  static final SymbolType INCLUDE = new SymbolType("INCLUDE");
  static final SymbolType ITALIC = new SymbolType("ITALIC");
  static final SymbolType LAST_MODIFIED = new SymbolType("LAST_MODIFIED");
  static final SymbolType LINK = new SymbolType("LINK");
  static final SymbolType LITERAL = new SymbolType("LITERAL");
  static final SymbolType LIST = new SymbolType("LIST");
  static final SymbolType META = new SymbolType("META");
  static final SymbolType NESTING = new SymbolType("NESTING");
  static final SymbolType NEW_LINE = new SymbolType("NEW_LINE");
  static final SymbolType NOTE = new SymbolType("NOTE");
  static final SymbolType PATH = new SymbolType("PATH");
  static final SymbolType PREFORMAT = new SymbolType("PREFORMAT");
  static final SymbolType SEE = new SymbolType("SEE");
  static final SymbolType STRIKE = new SymbolType("STRIKE");
  static final SymbolType STYLE = new SymbolType("STYLE");
  static final SymbolType TABLE = new SymbolType("TABLE");
  static final SymbolType TEXT = new SymbolType("TEXT");
  static final SymbolType TODAY = new SymbolType("TODAY");
  static final SymbolType WIKI_LINK = new SymbolType("WIKI_LINK");
  static final SymbolType WIKI_LIST = new SymbolType("WIKI_LIST");

  SymbolType(String name) {
    this.name = name;
  }

  public String toString() { return name; }

  private final String name;
}
