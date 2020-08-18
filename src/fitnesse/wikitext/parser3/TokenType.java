package fitnesse.wikitext.parser3;

public class TokenType {
  public static final TokenType ALIAS_END = new TokenType("AliasEnd", "]]");
  public static final TokenType ALIAS_MIDDLE = new TokenType("AliasMiddle", "][");
  public static final TokenType ALIAS_START = new TokenType("AliasStart", "[[");
  public static final TokenType ANCHOR_NAME = new TokenType("AnchorName", "!anchor");
  public static final TokenType ANCHOR_REFERENCE = new TokenType("AnchorReference", ".#");
  public static final TokenType BOLD = new TokenType("Bold", "'''");
  public static final TokenType BOLD_ITALIC = new TokenType("BoldItalic", "'''''");
  public static final TokenType BLANK_SPACE = new TokenType("BlankSpace", "blank space");
  public static final TokenType BRACE_END = new TokenType("BraceEnd", "}");
  public static final TokenType BRACE_START = new TokenType("BraceStart", "{");
  public static final TokenType BRACKET_END = new TokenType("BracketEnd", "]");
  public static final TokenType BRACKET_START = new TokenType("BracketStart", "[");
  public static final TokenType DEFINE = new TokenType("Define", "!define");
  public static final TokenType END = new TokenType("End");
  public static final TokenType ITALIC = new TokenType("Italic", "''");
  public static final TokenType LINK = new TokenType("Link", "link");
  public static final TokenType LITERAL_END = new TokenType("LiteralEnd", "-!");
  public static final TokenType LITERAL_START = new TokenType("LiteralStart", "!-");
  public static final TokenType NESTING_START = new TokenType("NestingStart", "!(");
  public static final TokenType NESTING_END = new TokenType("NestingEnd", ")!");
  public static final TokenType NEW_LINE = new TokenType("NewLine", "new line");
  public static final TokenType NOTE = new TokenType("Note", "!note");
  public static final TokenType PARENTHESIS_END = new TokenType("ParenthesisEnd", ")");
  public static final TokenType PARENTHESIS_START = new TokenType("ParenthesisStart", "(");
  public static final TokenType PATH = new TokenType("Path", "!path");
  public static final TokenType PREFORMAT_END = new TokenType("PreformatEnd", "}}}");
  public static final TokenType PREFORMAT_START = new TokenType("PreformatStart", "{{{");
  public static final TokenType SEE = new TokenType("See", "!see");
  public static final TokenType STRIKE = new TokenType("Strike", "--");
  public static final TokenType STYLE = new TokenType("Style", "!style_");
  public static final TokenType TEXT = new TokenType("Text");
  public static final TokenType VARIABLE = new TokenType("Variable", "${");
  public static final TokenType WIKI_PATH = new TokenType("WikiPath");

  public TokenType(String name) {
    this(name, name);
  }

  public TokenType(String name, String match) {
    this.match = match;
    this.name = name;
  }

  public String getMatch() { return match; }

  public String toString() { return name; }

  private final String name;
  private final String match;
}
