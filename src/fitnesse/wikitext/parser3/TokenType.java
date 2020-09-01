package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class TokenType {
  public static final TokenType ALIAS_END = new TokenType("AliasEnd").match("]]");
  public static final TokenType ALIAS_MIDDLE = new TokenType("AliasMiddle").match("][");
  public static final TokenType ALIAS_START = new TokenType("AliasStart")
    .match("[[")
    .rule(Alias::parse);
  public static final TokenType ANCHOR_NAME = new TokenType("AnchorName")
    .matchWord("!anchor")
    .rule(Keyword.parseWord(SymbolType.ANCHOR_NAME));
  public static final TokenType ANCHOR_REFERENCE = new TokenType("AnchorReference")
    .match(".#")
    .rule(Keyword.parseWord(SymbolType.ANCHOR_REFERENCE));
  public static final TokenType BOLD = new TokenType("Bold")
    .match("'''")
    .rule(Pair.parse(SymbolType.BOLD));
  public static final TokenType BOLD_ITALIC = new TokenType("BoldItalic")
    .match("'''''")
    .rule(Pair.parse(SymbolType.BOLD_ITALIC));
  public static final TokenType BLANK_SPACE = new TokenType("BlankSpace").matchBlank();
  public static final TokenType BRACE_END = new TokenType("BraceEnd").match("}");
  public static final TokenType BRACE_START = new TokenType("BraceStart").match("{");
  public static final TokenType BRACKET_END = new TokenType("BracketEnd").match("]");
  public static final TokenType BRACKET_START = new TokenType("BracketStart").match("[");
  public static final TokenType CELL_DELIMITER = new TokenType("CellDelimiter")
    .match("|")
    .rule(Table::parse);
  public static final TokenType CENTER_LINE = new TokenType("CenterLine").matchWord("!c");
  public static final TokenType COLLAPSIBLE_END = new TokenType("CollapsibleEnd").matchRepeatBefore("*","!");
  public static final TokenType COLLAPSIBLE_START = new TokenType("CollapsibleStart").matchRepeatAfter("!", "*");
  public static final TokenType COLON = new TokenType("Colon").match(":");
  public static final TokenType COMMA = new TokenType("Comma").match(",");
  public static final TokenType CONTENTS = new TokenType("Contents").match("!contents");
  public static final TokenType DEFINE = new TokenType("Define")
    .matchWord("!define")
    .rule(Variable::parsePut);
  public static final TokenType END = new TokenType("End");
  public static final TokenType HASH_TABLE = new TokenType("HashTable")
    .match("!{")
    .scan(Scanner::scanHashTable);
  public static final TokenType HEADER = new TokenType("Header")
    .matchWord("!1").matchWord("!2").matchWord("!3").matchWord("!4").matchWord("!5").matchWord("!6");
  public static final TokenType HORIZONTAL_RULE = new TokenType("HorizontalRule").matchRepeatAfter("---", "-");
  public static final TokenType HEADINGS = new TokenType("Headings").matchWord("!headings");
  public static final TokenType HELP = new TokenType("Help").match("!help");
  public static final TokenType IMAGE = new TokenType("Image").matchWord("!img");
  public static final TokenType INCLUDE = new TokenType("Include").matchWord("!include");
  public static final TokenType ITALIC = new TokenType("Italic")
    .match("''")
    .rule(Pair.parse(SymbolType.ITALIC));
  public static final TokenType LAST_MODIFIED = new TokenType("LastModified").match("!lastmodified");
  public static final TokenType LINK = new TokenType("Link")
    .match("http://")
    .match("https://")
    .rule(Link::parse);
  public static final TokenType LITERAL_END = new TokenType("LiteralEnd").match("-!");
  public static final TokenType LITERAL_START = new TokenType("LiteralStart")
    .match("!-")
    .scan(Scanner::scanLiteral);
  public static final TokenType META = new TokenType("Meta").matchWord("!meta");
  public static final TokenType NESTING_START = new TokenType("NestingStart").match("!(");
  public static final TokenType NESTING_END = new TokenType("NestingEnd").match(")!");
  public static final TokenType NEW_LINE = new TokenType("NewLine").match("\r\n").match("\n").match("\r");
  public static final TokenType NOTE = new TokenType("Note").matchWord("!note");
  public static final TokenType PARENTHESIS_END = new TokenType("ParenthesisEnd").match(")");
  public static final TokenType PARENTHESIS_START = new TokenType("ParenthesisStart").match("(");
  public static final TokenType PATH = new TokenType("Path")
    .matchWord("!path")
    .rule(Keyword.parse(SymbolType.PATH));
  public static final TokenType PLAIN_TEXT_TABLE_END = new TokenType("PlainTextTableEnd").match("]!");
  public static final TokenType PLAIN_TEXT_TABLE_START = new TokenType("PlainTextTableStart").match("![");
  public static final TokenType PREFORMAT_END = new TokenType("PreformatEnd").match("}}}");
  public static final TokenType PREFORMAT_START = new TokenType("PreformatStart")
    .match("{{{")
    .rule(Pair.parse(SymbolType.PREFORMAT));
  public static final TokenType SEE = new TokenType("See")
    .matchWord("!see")
    .rule(Keyword.parse(SymbolType.SEE));
  public static final TokenType STRIKE = new TokenType("Strike")
    .match("--")
    .rule(Pair.parse(SymbolType.STRIKE));
  public static final TokenType STYLE = new TokenType("Style")
    .match("!style_")
    .rule(Style::parse);
  public static final TokenType TEXT = new TokenType("Text");
  public static final TokenType TODAY = new TokenType("Today").match("!today");
  public static final TokenType VARIABLE = new TokenType("Variable")
    .match("${")
    .rule(Variable::parseGet);
  public static final TokenType WIKI_PATH = new TokenType("WikiPath")
    .rule(WikiPath::parse);

  public TokenType(String name) {
    this.match = name;
    this.name = name;
  }

  public TokenType scan(BiConsumer<Content, TokenList> scanner) {
    this.scanner = scanner;
    return this;
  }

  public TokenType match(String string) {
    this.match = string;
    readers.add(content -> matchString(content, string));
    return this;
  }

  public TokenType matchWord(String word) {
    this.match = word;
    readers.add(content -> {
      if (content.startsWith(word) && content.isBlankSpaceAt(word.length())) {
        content.advance(word.length());
        return word + readBlankSpace(content);
      }
      return "";
    });
    return this;
  }

  public TokenType matchRepeatAfter(String start, String repeat) {
    this.match = start + repeat;
    readers.add(content -> {
      if (content.startsWith(start)) {
        StringBuilder result = new StringBuilder(start);
        int repeatCount = 0;
        while (content.startsWith(repeat, start.length() + repeatCount)) {
          result.append(repeat);
          repeatCount++;
        }
        if (repeatCount > 0) {
          content.advance(result.length());
          return result.toString();
        }
      }
      return "";
    });
    return this;
  }

  public TokenType matchRepeatBefore(String repeat, String end) {
    this.match = repeat + end;
    readers.add(content -> {
      StringBuilder result = new StringBuilder();
      int repeatCount = 0;
      while (content.startsWith(repeat, repeatCount)) {
        result.append(repeat);
        repeatCount++;
      }
      if (repeatCount > 0 && content.startsWith(end, repeatCount)) {
        result.append(end);
        content.advance(result.length());
        return result.toString();
      }
      return "";
    });
    return this;
  }

  public TokenType matchBlank() {
    readers.add(TokenType::readBlankSpace);
    return this;
  }

  public TokenType rule(ParseRule parseRule) {
    this.parseRule = parseRule;
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

  public void scan(Content content, TokenList tokens) {
    scanner.accept(content, tokens);
  }

  public Symbol parse(Parser parser) {
    return parseRule.parse(parser);
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

  private String match;
  private BiConsumer<Content, TokenList> scanner = (content, tokens) -> {};
  private ParseRule parseRule = Parser::defaultRule;

  private final String name;
  private final List<Function<Content, String>> readers = new ArrayList<>(1);
}
