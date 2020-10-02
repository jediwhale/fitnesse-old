package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

import static fitnesse.wikitext.parser3.MatchContent.*;

public class TokenType {

  public static final TokenType ALIAS_END = new TokenType("AliasEnd", "]]");
  public static final TokenType ALIAS_MIDDLE = new TokenType("AliasMiddle", "][");
  public static final TokenType ALIAS_START = new TokenType("AliasStart", "[[")
    .rule(Alias::parse);
  public static final TokenType ANCHOR_NAME = new TokenType("AnchorName")
    .matches(word("!anchor"))
    .rule(Keyword.parseWord(SymbolType.ANCHOR_NAME));
  public static final TokenType ANCHOR_REFERENCE = new TokenType("AnchorReference", ".#")
    .rule(Keyword.parseWord(SymbolType.ANCHOR_REFERENCE));
  public static final TokenType BOLD = new TokenType("Bold", "'''")
    .rule(Pair.parse(SymbolType.BOLD));
  public static final TokenType BOLD_ITALIC = new TokenType("BoldItalic", "'''''")
    .rule(Pair.parse(SymbolType.BOLD_ITALIC));
  public static final TokenType BLANK_SPACE = new TokenType("BlankSpace").matches(blank());
  public static final TokenType BRACE_END = new TokenType("BraceEnd", "}");
  public static final TokenType BRACE_START = new TokenType("BraceStart", "{");
  public static final TokenType BRACKET_END = new TokenType("BracketEnd", "]");
  public static final TokenType BRACKET_START = new TokenType("BracketStart", "[");
  public static final TokenType CELL_DELIMITER = new TokenType("CellDelimiter", "|")
    .rule(Table::parse);
  public static final TokenType CENTER_LINE = new TokenType("CenterLine").matches(word("!c"));
  public static final TokenType COLLAPSIBLE_END = new TokenType("CollapsibleEnd", "*!").matches(repeat("*"), text("!"));
  public static final TokenType COLLAPSIBLE_START = new TokenType("CollapsibleStart").matches(text("!"), repeat("*"));
  public static final TokenType COLON = new TokenType("Colon", ":");
  public static final TokenType COMMA = new TokenType("Comma", ",");
  public static final TokenType COMMENT = new TokenType("Comment")
    .matches(startLine(), text("#"), endWith(matchOne(text("\r\n"), text("\n"), text("\r"))))
    .rule(Comment::parse);
  public static final TokenType CONTENTS = new TokenType("Contents").matches(word("!contents"));
  public static final TokenType DEFINE = new TokenType("Define")
    .matches(word("!define"))
    .rule(Variable::parsePut);
  public static final TokenType END = new TokenType("End");
  public static final TokenType HASH_TABLE = new TokenType("HashTable", "!{")
    .useScan(HashTable::scan);
  public static final TokenType HEADER = new TokenType("Header")
    .matchOneOf(word("!1"), word("!2"), word("!3"), word("!4"), word("!5"), word("!6"));
  public static final TokenType HORIZONTAL_RULE = new TokenType("HorizontalRule").matches(text("---"), repeat("-"));
  public static final TokenType HEADINGS = new TokenType("Headings").matches(word("!headings"));
  public static final TokenType HELP = new TokenType("Help").matches(word("!help"));
  public static final TokenType IMAGE = new TokenType("Image").matches(word("!img"));
  public static final TokenType INCLUDE = new TokenType("Include")
    .matches(word("!include"))
    .rule(Include::parse);
  public static final TokenType ITALIC = new TokenType("Italic", "''")
    .rule(Pair.parse(SymbolType.ITALIC));
  public static final TokenType LAST_MODIFIED = new TokenType("LastModified").matches(word("!lastmodified"));
  public static final TokenType LINK = new TokenType("Link")
    .matchOneOf(text("http://"), text("https://"))
    .rule(Link::parse);
  public static final TokenType LITERAL_END = new TokenType("LiteralEnd", "-!");
  public static final TokenType LITERAL_START = new TokenType("LiteralStart", "!-")
    .useScan(LITERAL_END)
    .rule(Literal::parse);
  public static final TokenType META = new TokenType("Meta").matches(word("!meta"));
  public static final TokenType NESTING_START = new TokenType("NestingStart", "!(")
    .rule(Nesting::parse);
  public static final TokenType NESTING_END = new TokenType("NestingEnd", ")!");
  public static final TokenType NEW_LINE = new TokenType("NewLine")
    .matchOneOf(text("\r\n"), text("\n"), text("\r"));
  public static final TokenType NOTE = new TokenType("Note").matches(word("!note"));
  public static final TokenType PARENTHESIS_END = new TokenType("ParenthesisEnd", ")");
  public static final TokenType PARENTHESIS_START = new TokenType("ParenthesisStart", "(");
  public static final TokenType PATH = new TokenType("Path")
    .matches(startLine(), word("!path"))
    .rule(Path::parse);
  public static final TokenType PLAIN_TEXT_TABLE_END = new TokenType("PlainTextTableEnd", "]!");
  public static final TokenType PLAIN_TEXT_TABLE_START = new TokenType("PlainTextTableStart", "![");
  public static final TokenType PREFORMAT_END = new TokenType("PreformatEnd", "}}}");
  public static final TokenType PREFORMAT_START = new TokenType("PreformatStart", "{{{")
    .useScan(PREFORMAT_END)
    .rule(Preformat::parse);
  public static final TokenType SEE = new TokenType("See")
    .matches(word("!see"))
    .rule(Keyword.parse(SymbolType.SEE));
  public static final TokenType STRIKE = new TokenType("Strike", "--")
    .rule(Pair.parse(SymbolType.STRIKE));
  public static final TokenType STYLE = new TokenType("Style", "!style_")
    .rule(Style::parse);
  public static final TokenType TEXT = new TokenType("Text");
  public static final TokenType TODAY = new TokenType("Today").matches(word("!today"));
  public static final TokenType VARIABLE = new TokenType("Variable", "${")
    .rule(Variable::parseGet);

  public TokenType(String name, String match) {
    this.match = match;
    this.name = name;
    matcher = text(match);
  }

  public TokenType(String name) {
    this.name = name;
    this.match = name;
    matcher = content -> Optional.empty();
  }

  public TokenType useScan(TokenType terminator) {
    return useScan(source -> source.use(new ArrayList<>(Collections.singletonList(terminator)), type -> type == terminator));
  }

  public TokenType useScan(Consumer<TokenSource> useScan) {
    this.useScan = useScan;
    return this;
  }

  public TokenType rule(ParseRule parseRule) {
    this.parseRule = parseRule;
    return this;
  }

  public Token asToken(String content) { return new Token(this, content); }
  public String getMatch() { return match; }
  public String toString() { return name; }

  public Optional<String> read(Content content) {
    return matcher.check(content);
  }

  public void useScan(TokenSource source) { useScan.accept(source); }

  public Symbol parse(Parser parser) {
    return parseRule.parse(parser);
  }

  private TokenType matches(MatchContent... matchItems) {
    matcher = matchAll(matchItems);
    return this;
  }

  private TokenType matchOneOf(MatchContent... matchItems) {
    matcher = matchOne(matchItems);
    return this;
  }

  private Consumer<TokenSource> useScan = s -> {};
  private ParseRule parseRule = Parser::defaultRule;
  private MatchContent matcher;

  private final String match;
  private final String name;
}
