package fitnesse.wikitext.parser3;

import java.util.function.Function;

public class TokenType {
  public static final TokenType ALIAS_END = new TokenType("AliasEnd", "]]").matchString();
  public static final TokenType ALIAS_MIDDLE = new TokenType("AliasMiddle", "][").matchString();
  public static final TokenType ALIAS_START = new TokenType("AliasStart", "[[").matchString();
  public static final TokenType ANCHOR_NAME = new TokenType("AnchorName", "!anchor").matchWord();
  public static final TokenType ANCHOR_REFERENCE = new TokenType("AnchorReference", ".#").matchString();
  public static final TokenType BOLD = new TokenType("Bold", "'''").matchString();
  public static final TokenType BOLD_ITALIC = new TokenType("BoldItalic", "'''''").matchString();
  public static final TokenType BLANK_SPACE = new TokenType("BlankSpace", "blank space").matchBlank();
  public static final TokenType BRACE_END = new TokenType("BraceEnd", "}").matchString();
  public static final TokenType BRACE_START = new TokenType("BraceStart", "{").matchString();
  public static final TokenType BRACKET_END = new TokenType("BracketEnd", "]").matchString();
  public static final TokenType BRACKET_START = new TokenType("BracketStart", "[").matchString();
  public static final TokenType CELL_DELIMITER = new TokenType("CellDelimiter", "|").matchString();
  public static final TokenType DEFINE = new TokenType("Define", "!define").matchWord();
  public static final TokenType END = new TokenType("End");
  public static final TokenType ITALIC = new TokenType("Italic", "''").matchString();
  public static final TokenType LINK = new TokenType("Link", "link").matchStrings(new String[]{"http://", "https://"});
  public static final TokenType LITERAL_END = new TokenType("LiteralEnd", "-!").matchString();
  public static final TokenType LITERAL_START = new TokenType("LiteralStart", "!-").matchString();
  public static final TokenType NESTING_START = new TokenType("NestingStart", "!(").matchString();
  public static final TokenType NESTING_END = new TokenType("NestingEnd", ")!").matchString();
  public static final TokenType NEW_LINE = new TokenType("NewLine", "new line").matchStrings(new String[]{"\r\n", "\n", "\r"});
  public static final TokenType NOTE = new TokenType("Note", "!note").matchWord();
  public static final TokenType PARENTHESIS_END = new TokenType("ParenthesisEnd", ")").matchString();
  public static final TokenType PARENTHESIS_START = new TokenType("ParenthesisStart", "(").matchString();
  public static final TokenType PATH = new TokenType("Path", "!path").matchWord();
  public static final TokenType PREFORMAT_END = new TokenType("PreformatEnd", "}}}").matchString();
  public static final TokenType PREFORMAT_START = new TokenType("PreformatStart", "{{{").matchString();
  public static final TokenType SEE = new TokenType("See", "!see").matchWord();
  public static final TokenType STRIKE = new TokenType("Strike", "--").matchString();
  public static final TokenType STYLE = new TokenType("Style", "!style_").matchString();
  public static final TokenType TEXT = new TokenType("Text");
  public static final TokenType VARIABLE = new TokenType("Variable", "${").matchString();
  public static final TokenType WIKI_PATH = new TokenType("WikiPath");

  public TokenType(String name) {
    this(name, name);
  }

  public TokenType(String name, String match) {
    this.match = match;
    this.name = name;
  }

  public TokenType matchString() {
    reader = content -> content.readString(match) ? match : "";
    return this;
  }

  public TokenType matchStrings(String[] matches) {
    reader = strings(matches);
    return this;
  }

  public TokenType matchWord() {
    reader = content -> content.readWord(match);
    return this;
  }

  public TokenType matchBlank() {
    reader = Content::readBlankSpace;
    return this;
  }

  public Token asToken(String content) { return new Token(this, content); }

  public String getMatch() { return match; }
  public String toString() { return name; }
  public String read(Content content) { return reader.apply(content); }

  private static Function<Content, String> strings(String[] matches) {
    return content -> {
      for (String match : matches) {
        if (content.readString(match)) return match;
      }
      return "";
    };
  }

  private final String name;
  private final String match;
  private Function<Content, String> reader;
}
