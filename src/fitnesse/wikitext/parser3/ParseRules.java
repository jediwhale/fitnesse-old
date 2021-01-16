package fitnesse.wikitext.parser3;

import fitnesse.wikitext.parser.TextMaker;
import fitnesse.wikitext.shared.ParsingPage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

class ParseRules {

  ParseRules(ParsingPage page) {
    rules = make(page);
    makeSymbolFromText = ParseRules::determineTextSymbol;
  }

  ParseRules withTextType(SymbolType textType) {
    return new ParseRules(this, textType);
  }

  private ParseRules(ParseRules other, SymbolType textType) {
    rules = other.rules;
    makeSymbolFromText = (c, o) -> makeLeaf(textType, c, o);
  }

  Symbol parse(Parser parser) {
    return rules.getOrDefault(parser.peek(0).getType(), this::defaultRule).parse(parser);
  }

  static Map<TokenType, ParseRule> make(ParsingPage page) {
    Map<TokenType, ParseRule> rules = new HashMap<>();
    rules.put(DelimiterType.ALIAS_START, Alias::parse);
    rules.put(KeywordType.ANCHOR_NAME, Keyword.parseBlankAndWord(SymbolType.ANCHOR_NAME));
    rules.put(DelimiterType.ANCHOR_REFERENCE, Keyword.parseWord(SymbolType.ANCHOR_REFERENCE));
    rules.put(DelimiterType.BOLD, Pair.parse(SymbolType.BOLD));
    rules.put(DelimiterType.BOLD_ITALIC, Pair.parse(SymbolType.BOLD_ITALIC));
    rules.put(DelimiterType.BULLET_LIST, WikiList::parse);
    rules.put(KeywordType.CENTER, Line.parse(SymbolType.CENTER));
    rules.put(DelimiterType.COLLAPSIBLE_START, Collapsible::parse);
    rules.put(DelimiterType.COMMENT, Comment::parse);
    rules.put(KeywordType.CONTENTS, parser -> Contents.parse(parser, page));
    rules.put(KeywordType.DEFINE, parser -> Define.parse(parser, page));
    rules.put(KeywordType.DEFINE_NESTED, Define::parseNested);
    rules.put(DelimiterType.EXPRESSION_START, parser -> Expression.parse(parser, page));
    rules.put(DelimiterType.HASH_TABLE, HashTable::parse);
    rules.put(KeywordType.HEADER, parser -> Header.parse(parser, page));
    rules.put(KeywordType.HEADINGS, Headings::parse);
    rules.put(KeywordType.HELP, Help::parse);
    rules.put(KeywordType.INCLUDE, parser -> Include.parse(parser, page, page));
    rules.put(KeywordType.IMAGE, Image::parse);
    rules.put(DelimiterType.ITALIC, Pair.parse(SymbolType.ITALIC));
    rules.put(KeywordType.LAST_MODIFIED, makeType(SymbolType.LAST_MODIFIED));
    rules.put(DelimiterType.LINK, Link::parse);
    rules.put(DelimiterType.LITERAL_START, Literal::parse);
    rules.put(KeywordType.META, Line.parse(SymbolType.META));
    rules.put(DelimiterType.NESTING_START, Nesting::parse);
    rules.put(DelimiterType.NESTING_PSEUDO_START, Nesting::parse);
    rules.put(DelimiterType.NEW_LINE, makeType(SymbolType.NEW_LINE));
    rules.put(KeywordType.NOTE, Line.parse(SymbolType.NOTE));
    rules.put(DelimiterType.NUMBERED_LIST, WikiList::parse);
    rules.put(KeywordType.PATH, Path::parse);
    rules.put(DelimiterType.PLAIN_TEXT_TABLE_START, Table::parsePlain);
    rules.put(DelimiterType.PREFORMAT_START, Preformat::parse);
    rules.put(KeywordType.SEE, Keyword.parse(SymbolType.SEE));
    rules.put(DelimiterType.STRIKE, Pair.parse(SymbolType.STRIKE));
    rules.put(DelimiterType.STYLE, Style::parse);
    rules.put(KeywordType.TODAY, Today::parse);
    rules.put(DelimiterType.TABLE_START, Table::parseStandard);
    return rules;
  }

  static ParseRule makeType(SymbolType symbolType) {
    return parser -> new LeafSymbol(symbolType, parser.advance().getContent());
  }

  private Symbol defaultRule(Parser parser) {
    String content = parser.peek(0).getContent();
    Symbol result = makeSymbolFromText.apply(content, parser.peek(0).getOffset());
    parser.advance();
    return result;
  }

  private static Symbol determineTextSymbol(String text, Integer offset) {
    int wikiWordLength = TextMaker.findWikiWordLength(text);
    SymbolType type =
      wikiWordLength > 0 ? SymbolType.WIKI_LINK
        : isEMail(text) ? SymbolType.EMAIL
        : SymbolType.TEXT;
    if (wikiWordLength == 0 || wikiWordLength == text.length()) {
      return makeLeaf(type, text, offset);
    }
    Symbol result = new BranchSymbol(SymbolType.LIST);
    result.add(makeLeaf(SymbolType.WIKI_LINK, text.substring(0, wikiWordLength), offset));
    result.add(determineTextSymbol(text.substring(wikiWordLength), offset + wikiWordLength));
    return result;
  }

  private static boolean isEMail(String text) {
    return text.indexOf("@") > 0 && Pattern.matches(EMAIL_PATTERN, text);
  }

  private static Symbol makeLeaf(SymbolType type, String text, Integer offset) {
    return new LeafSymbol(type, text, offset);
  }

  private static final String EMAIL_PATTERN = "[\\w-_.]+@[\\w-_.]+\\.[\\w-_.]+";

  private final Map<TokenType, ParseRule> rules;
  private final BiFunction<String, Integer, Symbol> makeSymbolFromText;
}
