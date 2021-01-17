package fitnesse.wikitext.parser3;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

import static fitnesse.wikitext.parser3.MatchContent.blank;
import static fitnesse.wikitext.parser3.MatchContent.digit;
import static fitnesse.wikitext.parser3.MatchContent.end;
import static fitnesse.wikitext.parser3.MatchContent.endWith;
import static fitnesse.wikitext.parser3.MatchContent.ignoreBlank;
import static fitnesse.wikitext.parser3.MatchContent.matchAll;
import static fitnesse.wikitext.parser3.MatchContent.matchOne;
import static fitnesse.wikitext.parser3.MatchContent.newLine;
import static fitnesse.wikitext.parser3.MatchContent.notText;
import static fitnesse.wikitext.parser3.MatchContent.repeat;
import static fitnesse.wikitext.parser3.MatchContent.startLine;
import static fitnesse.wikitext.parser3.MatchContent.text;
import static fitnesse.wikitext.parser3.MatchContent.variableToken;
import static fitnesse.wikitext.parser3.MatchContent.variableValue;

class DelimiterType extends TokenType {

  static final DelimiterType ALIAS_END = new DelimiterType("AliasEnd", "]]");
  static final DelimiterType ALIAS_MIDDLE = new DelimiterType("AliasMiddle", "][")
    .useScan(Alias::scan);
  static final DelimiterType ALIAS_START = new DelimiterType("AliasStart", "[[");
  static final DelimiterType ANCHOR_REFERENCE = new DelimiterType("AnchorReference", ".#");
  static final DelimiterType BLANK_SPACE = new DelimiterType("BlankSpace").matches(blank());
  static final DelimiterType BOLD = new DelimiterType("Bold", "'''");
  static final DelimiterType BOLD_ITALIC = new DelimiterType("BoldItalic", "'''''");
  static final DelimiterType BRACE_END = new DelimiterType("BraceEnd", "}");
  static final DelimiterType BRACE_START = new DelimiterType("BraceStart", "{");
  static final DelimiterType BRACKET_END = new DelimiterType("BracketEnd", "]");
  static final DelimiterType BULLET_LIST = new DelimiterType("BulletList")
    .matches(startLine(), blank(), text("*"));
  static final DelimiterType BRACKET_START = new DelimiterType("BracketStart", "[");
  static final DelimiterType CELL_DELIMITER = new DelimiterType("CellDelimiter", "|")
    .matchOneOf(
      matchAll(text("|"), ignoreBlank(), newLine(), text("|")),
      matchAll(text("|"), ignoreBlank()));
  static final DelimiterType COLLAPSIBLE_END = new DelimiterType("CollapsibleEnd", "*!").matches(repeat("*"), text("!"));
  static final DelimiterType COLLAPSIBLE_START = new DelimiterType("CollapsibleStart").matches(text("!"), repeat("*"));
  static final DelimiterType COLON = new DelimiterType("Colon", ":");
  static final DelimiterType COMMA = new DelimiterType("Comma", ",");
  static final DelimiterType COMMENT = new DelimiterType("Comment")
    .matches(startLine(), text("#"), endWith(newLine()))
    .isStart();
  static final DelimiterType EXPRESSION_END = new DelimiterType("ExpressionEnd", "=}");
  static final DelimiterType EXPRESSION_START = new DelimiterType("ExpressionStart", "${=");
  static final DelimiterType HASH_TABLE = new DelimiterType("HashTable", "!{")
    .useScan(HashTable::scan);
  static final DelimiterType HORIZONTAL_RULE = new DelimiterType("HorizontalRule").matches(text("---"), repeat("-"));
  static final DelimiterType ITALIC = new DelimiterType("Italic", "''");
  static final DelimiterType LINK = new DelimiterType("Link")
    .matchOneOf(text("http://"), text("https://"));
  static final DelimiterType LITERAL_END = new DelimiterType("LiteralEnd", "-!");
  static final DelimiterType LITERAL_START = new DelimiterType("LiteralStart", "!-")
    .useScan(LITERAL_END);
  static final DelimiterType NESTING_START = new DelimiterType("NestingStart", "!(")
    .isStart();
  static final DelimiterType NESTING_END = new DelimiterType("NestingEnd", ")!");
  static final DelimiterType NESTING_PSEUDO_START = new DelimiterType("NestingPseudoStart", String.valueOf(Nesting.START))
    .isStart();
  static final DelimiterType NESTING_PSEUDO_END = new DelimiterType("NestingPseudoEnd", String.valueOf(Nesting.END));
  static final DelimiterType NEW_LINE = new DelimiterType("NewLine")
    .matches(newLine())
    .isStart();
  static final DelimiterType NUMBERED_LIST = new DelimiterType("NumberedList")
    .matches(startLine(), blank(), digit());
  static final DelimiterType PARENTHESIS_END = new DelimiterType("ParenthesisEnd", ")");
  static final DelimiterType PARENTHESIS_START = new DelimiterType("ParenthesisStart", "(");
  static final DelimiterType PLAIN_TEXT_TABLE_END = new DelimiterType("PlainTextTableEnd", "]!");
  static final DelimiterType PLAIN_TEXT_TABLE_START = new DelimiterType("PlainTextTableStart", "![");
  static final DelimiterType PREFORMAT_END = new DelimiterType("PreformatEnd", "}}}");
  static final DelimiterType STRIKE = new DelimiterType("Strike", "--");
  static final DelimiterType TABLE_START = new DelimiterType("TableStart")
    .matches(startLine(), matchOne(text("-!|"), text("!|"), text("-^|"), text("^|"), text("-|"), text("|")));
  static final DelimiterType TABLE_END = new DelimiterType("TableEnd")
    .matches(text("|"), ignoreBlank(),  matchOne(end(), matchAll(newLine(), notText("|"))))
    .isStart();
  static final DelimiterType PREFORMAT_START = new DelimiterType("PreformatStart", "{{{")
    .useScan(Preformat::scan);
  static final DelimiterType VARIABLE_VALUE = new DelimiterType("Variable")
    .matches(variableValue()); //this is used when a variable value is looked up and substituted for the variable token
  static final DelimiterType VARIABLE_TOKEN = new DelimiterType("Variable")
    .matches(variableToken()); //this is used when the variable token is kept as is

  DelimiterType(String name, String match) {
    super(name, match);
    matcher = text(match);
  }

  DelimiterType(String name) {
    super(name);
    matcher = content -> Optional.empty();
  }

  Optional<String> read(Content content, TokenSource source) {
    Optional<String> result = matcher.check(content);
    if (result.isPresent()) {
      if (isStart) content.setStartLine();
      useScan.accept(source);
    }
    return result;
  }

  boolean check(Content content) {
    return matcher.check(content).isPresent();
  }

  DelimiterType useScan(DelimiterType terminator) {
    return useScan(source -> source.use(new TokenTypes(Collections.singletonList(terminator)), type -> type == terminator));
  }

  DelimiterType matches(MatchContent... matchItems) {
    matcher = matchItems.length == 1 ? matchItems[0] : matchAll(matchItems);
    return this;
  }

  DelimiterType matchOneOf(MatchContent... matchItems) {
    matcher = matchOne(matchItems);
    return this;
  }

  DelimiterType isStart() {
    isStart = true;
    return this;
  }

  DelimiterType useScan(Consumer<TokenSource> useScan) {
    this.useScan = useScan;
    return this;
  }

  private MatchContent matcher;
  private boolean isStart = false;
  private Consumer<TokenSource> useScan = s -> {};
}
