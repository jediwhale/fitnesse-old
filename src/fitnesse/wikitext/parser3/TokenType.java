package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TokenType {
  public static final TokenType ALIAS_END = new TokenType("AliasEnd").match("]]");
  public static final TokenType ALIAS_MIDDLE = new TokenType("AliasMiddle").match("][");
  public static final TokenType ALIAS_START = new TokenType("AliasStart").match("[[");
  public static final TokenType ANCHOR_NAME = new TokenType("AnchorName").matchWord("!anchor");
  public static final TokenType ANCHOR_REFERENCE = new TokenType("AnchorReference").match(".#");
  public static final TokenType BOLD = new TokenType("Bold").match("'''");
  public static final TokenType BOLD_ITALIC = new TokenType("BoldItalic").match("'''''");
  public static final TokenType BLANK_SPACE = new TokenType("BlankSpace").matchBlank();
  public static final TokenType BRACE_END = new TokenType("BraceEnd").match("}");
  public static final TokenType BRACE_START = new TokenType("BraceStart").match("{");
  public static final TokenType BRACKET_END = new TokenType("BracketEnd").match("]");
  public static final TokenType BRACKET_START = new TokenType("BracketStart").match("[");
  public static final TokenType CELL_DELIMITER = new TokenType("CellDelimiter").match("|");
  public static final TokenType CENTER_LINE = new TokenType("CenterLine").matchWord("!c");
  public static final TokenType DEFINE = new TokenType("Define").matchWord("!define");
  public static final TokenType END = new TokenType("End");
  public static final TokenType HEADER = new TokenType("Header")
    .matchWord("!1").matchWord("!2").matchWord("!3").matchWord("!4").matchWord("!5").matchWord("!6");
  public static final TokenType HEADINGS = new TokenType("Headings").matchWord("!headings");
  public static final TokenType IMAGE = new TokenType("Image").matchWord("!img");
  public static final TokenType INCLUDE = new TokenType("Include").matchWord("!include");
  public static final TokenType ITALIC = new TokenType("Italic").match("''");
  public static final TokenType LINK = new TokenType("Link").match("http://").match("https://");
  public static final TokenType LITERAL_END = new TokenType("LiteralEnd").match("-!");
  public static final TokenType LITERAL_START = new TokenType("LiteralStart").match("!-");
  public static final TokenType META = new TokenType("Meta").matchWord("!meta");
  public static final TokenType NESTING_START = new TokenType("NestingStart").match("!(");
  public static final TokenType NESTING_END = new TokenType("NestingEnd").match(")!");
  public static final TokenType NEW_LINE = new TokenType("NewLine").match("\r\n").match("\n").match("\r");
  public static final TokenType NOTE = new TokenType("Note").matchWord("!note");
  public static final TokenType PARENTHESIS_END = new TokenType("ParenthesisEnd").match(")");
  public static final TokenType PARENTHESIS_START = new TokenType("ParenthesisStart").match("(");
  public static final TokenType PATH = new TokenType("Path").matchWord("!path");
  public static final TokenType PREFORMAT_END = new TokenType("PreformatEnd").match("}}}");
  public static final TokenType PREFORMAT_START = new TokenType("PreformatStart").match("{{{");
  public static final TokenType SEE = new TokenType("See").matchWord("!see");
  public static final TokenType STRIKE = new TokenType("Strike").match("--");
  public static final TokenType STYLE = new TokenType("Style").match("!style_");
  public static final TokenType TEXT = new TokenType("Text");
  public static final TokenType VARIABLE = new TokenType("Variable").match("${");
  public static final TokenType WIKI_PATH = new TokenType("WikiPath");

  public TokenType(String name) {
    this.match = name;
    this.name = name;
  }

  public TokenType match(String match) {
    this.match = match;
    readers.add(content -> matchString(content, match));
    return this;
  }

  public TokenType matchWord(String match) {
    this.match = match;
    readers.add(content -> {
      if (content.startsWith(match) && content.isBlankSpaceAt(match.length())) {
        content.advance(match.length());
        return match + readBlankSpace(content);
      }
      return "";
    });
    return this;
  }

  public TokenType matchBlank() {
    readers.add(TokenType::readBlankSpace);
    return this;
  }

  public Token asToken(String content) { return new Token(this, content); }

  public String getMatch() { return match; }
  public String toString() { return name; }

  public String read(Content content) {
    for (Function<Content, String>reader : readers) {
      String result = reader.apply(content);
      if (result.length() > 0) return result;
    }
    return "";
  }

  private static String matchString(Content content, String match) {
    if (content.startsWith(match)) {
      content.advance(match.length());
      return match;
    }
    return "";
  }

  private static String readBlankSpace(Content content) {
    StringBuilder result = new StringBuilder();
    while (content.isBlankSpaceAt(0)) {
      result.append(content.advance());
    }
    return result.toString();
  }

  private final String name;
  private final List<Function<Content, String>> readers = new ArrayList<>(1);
  private String match;
}
