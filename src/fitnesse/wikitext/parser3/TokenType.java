package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.function.BiConsumer;

import static fitnesse.wikitext.parser3.MatchContent.*;

public class TokenType {

  public static final TokenType ALIAS_END = new TokenType("AliasEnd", "]]");
  public static final TokenType ALIAS_MIDDLE = new TokenType("AliasMiddle", "][")
    .useScan(Alias::scan);
  public static final TokenType ALIAS_START = new TokenType("AliasStart", "[[");
  public static final TokenType ANCHOR_NAME = new TokenType("AnchorName")
    .matches(word("!anchor"));
  public static final TokenType ANCHOR_REFERENCE = new TokenType("AnchorReference", ".#");
  public static final TokenType BOLD = new TokenType("Bold", "'''");
  public static final TokenType BOLD_ITALIC = new TokenType("BoldItalic", "'''''");
  public static final TokenType BLANK_SPACE = new TokenType("BlankSpace").matches(blank());
  public static final TokenType BRACE_END = new TokenType("BraceEnd", "}");
  public static final TokenType BRACE_START = new TokenType("BraceStart", "{");
  public static final TokenType BRACKET_END = new TokenType("BracketEnd", "]");
  public static final TokenType BRACKET_START = new TokenType("BracketStart", "[");
  public static final TokenType BULLET_LIST = new TokenType("BulletList")
    .matches(startLine(), blank(), text("*"));
  public static final TokenType CELL_DELIMITER = new TokenType("CellDelimiter", "|")
    .matchOneOf(
      matchAll(text("|"), ignoreBlank(), newLine(), text("|")),
      matchAll(text("|"), ignoreBlank()));
  public static final TokenType CENTER = new TokenType("Center")
    .matches(startLine(), word("!c"));
  public static final TokenType COLLAPSIBLE_END = new TokenType("CollapsibleEnd", "*!").matches(repeat("*"), text("!"));
  public static final TokenType COLLAPSIBLE_START = new TokenType("CollapsibleStart").matches(text("!"), repeat("*"));
  public static final TokenType COLON = new TokenType("Colon", ":");
  public static final TokenType COMMA = new TokenType("Comma", ",");
  public static final TokenType COMMENT = new TokenType("Comment")
    .matches(startLine(), text("#"), endWith(newLine()))
    .isStart();
  public static final TokenType CONTENTS = new TokenType("Contents", "!contents");
  public static final TokenType DEFINE = new TokenType("Define").matches(word("!define"));
  public static final TokenType DEFINE_NESTED = new TokenType("Define").matches(word("!define"));
  public static final TokenType EXPRESSION_END = new TokenType("ExpressionEnd", "=}");
  public static final TokenType EXPRESSION_START = new TokenType("ExpressionStart", "${=");
  public static final TokenType END = new TokenType("End");
  public static final TokenType HASH_TABLE = new TokenType("HashTable", "!{")
    .useScan(HashTable::scan);
  public static final TokenType HEADER = new TokenType("Header")
    .matches(startLine(), matchOne(word("!1"), word("!2"), word("!3"), word("!4"), word("!5"), word("!6")));
  public static final TokenType HEADINGS = new TokenType("Headings")
    .matches(startLine(), text("!headings"));
  public static final TokenType HORIZONTAL_RULE = new TokenType("HorizontalRule").matches(text("---"), repeat("-"));
  public static final TokenType HELP = new TokenType("Help").matches(text("!help"));
  public static final TokenType IMAGE = new TokenType("Image")
    .matchOneOf(word("!img"), word("!img-l"), word("!img-r"));
  public static final TokenType INCLUDE = new TokenType("Include")
    .matches(word("!include"));
  public static final TokenType ITALIC = new TokenType("Italic", "''");
  public static final TokenType LAST_MODIFIED = new TokenType("LastModified","!lastmodified"); //todo: some kind of terminator
  public static final TokenType LINK = new TokenType("Link")
    .matchOneOf(text("http://"), text("https://"));
  public static final TokenType LITERAL_END = new TokenType("LiteralEnd", "-!");
  public static final TokenType LITERAL_START = new TokenType("LiteralStart", "!-")
    .useScan(LITERAL_END);
  public static final TokenType META = new TokenType("Meta").matches(word("!meta"));
  public static final TokenType NESTING_START = new TokenType("NestingStart", "!(")
    .isStart();
  public static final TokenType NESTING_END = new TokenType("NestingEnd", ")!");
  public static final TokenType NESTING_PSEUDO_START = new TokenType("NestingPseudoStart", String.valueOf(Nesting.START))
    .isStart();
  public static final TokenType NESTING_PSEUDO_END = new TokenType("NestingPseudoEnd", String.valueOf(Nesting.END));
  public static final TokenType NEW_LINE = new TokenType("NewLine")
    .matches(newLine())
    .isStart();
  public static final TokenType NUMBERED_LIST = new TokenType("NumberedList")
    .matches(startLine(), blank(), digit());
  public static final TokenType NOTE = new TokenType("Note").matches(word("!note"));
  public static final TokenType PARENTHESIS_END = new TokenType("ParenthesisEnd", ")");
  public static final TokenType PARENTHESIS_START = new TokenType("ParenthesisStart", "(");
  public static final TokenType PATH = new TokenType("Path")
    .matches(startLine(), word("!path"));
  public static final TokenType PLAIN_TEXT_TABLE_END = new TokenType("PlainTextTableEnd", "]!");
  public static final TokenType PLAIN_TEXT_TABLE_START = new TokenType("PlainTextTableStart", "![");
  public static final TokenType PREFORMAT_END = new TokenType("PreformatEnd", "}}}");
  public static final TokenType PREFORMAT_START = new TokenType("PreformatStart", "{{{")
    .useScan(PREFORMAT_END);
  public static final TokenType SEE = new TokenType("See")
    .matches(word("!see"));
  public static final TokenType STRIKE = new TokenType("Strike", "--");
  public static final TokenType STYLE = new TokenType("Style", "!style_");
  public static final TokenType TABLE_START = new TokenType("TableStart")
    .matches(startLine(), matchOne(text("-!|"), text("!|"), text("-|"), text("|")));
  public static final TokenType TABLE_END = new TokenType("TableEnd")
    .matches(text("|"), ignoreBlank(),  matchOne(end(), matchAll(newLine(), notText("|"))))
    .isStart();
  public static final TokenType TEXT = new TokenType("Text");
  public static final TokenType TODAY = new TokenType("Today").matches(word("!today"));
  public static final TokenType VARIABLE_VALUE = new TokenType("Variable")
    .matches(variableValue()); //this is used when a variable value is looked up and substituted for the variable token
  public static final TokenType VARIABLE_TOKEN = new TokenType("Variable")
    .matches(variableToken()); //this is used when the variable token is kept as is

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
    return useScan((token, source) -> source.use(new ArrayList<>(Collections.singletonList(terminator)), type -> type == terminator));
  }

  public TokenType useScan(BiConsumer<Token, TokenSource> useScan) { //todo: eliminate this??
    this.useScan = useScan;
    return this;
  }

  public TokenType isStart() {
    isStart = true;
    return this;
  }

  public String getMatch() { return match; }
  public String toString() { return name; }

  public Optional<Token> read(Content content) {
    Optional<String> result = matcher.check(content);
    if (result.isPresent() && isStart) content.setStartLine();
    return result.map(s -> new Token(this, s));
  }

  public void useScan(Token token, TokenSource source) { useScan.accept(token, source); }

  private TokenType matches(MatchContent... matchItems) {
    matcher = matchItems.length == 1 ? matchItems[0] : matchAll(matchItems);
    return this;
  }

  private TokenType matchOneOf(MatchContent... matchItems) {
    matcher = matchOne(matchItems);
    return this;
  }

  private BiConsumer<Token, TokenSource> useScan = (t, s) -> {};
  private MatchContent matcher;
  private boolean isStart = false;

  private final String match;
  private final String name;
}
