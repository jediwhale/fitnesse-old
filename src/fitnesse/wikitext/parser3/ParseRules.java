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
    rules.put(TokenType.ALIAS_START, Alias::parse);
    rules.put(TokenType.ANCHOR_NAME, Keyword.parseWord(SymbolType.ANCHOR_NAME));
    rules.put(TokenType.ANCHOR_REFERENCE, Keyword.parseWord(SymbolType.ANCHOR_REFERENCE));
    rules.put(TokenType.BOLD, Pair.parse(SymbolType.BOLD));
    rules.put(TokenType.BOLD_ITALIC, Pair.parse(SymbolType.BOLD_ITALIC));
    rules.put(TokenType.BULLET_LIST, WikiList::parse);
    rules.put(TokenType.CENTER, Line.parse(SymbolType.CENTER));
    rules.put(TokenType.COLLAPSIBLE_START, Collapsible::parse);
    rules.put(TokenType.COMMENT, Comment::parse);
    rules.put(TokenType.CONTENTS, parser -> Contents.parse(parser, page));
    rules.put(TokenType.DEFINE, parser -> Define.parse(parser, page));
    rules.put(TokenType.DEFINE_NESTED, Define::parseNested);
    rules.put(TokenType.EXPRESSION_START, parser -> Expression.parse(parser, page));
    rules.put(TokenType.HASH_TABLE, HashTable::parse);
    rules.put(TokenType.HEADER, parser -> Header.parse(parser, page));
    rules.put(TokenType.HEADINGS, Headings::parse);
    rules.put(TokenType.HELP, Help::parse);
    rules.put(TokenType.INCLUDE, parser -> Include.parse(parser, page, page));
    rules.put(TokenType.IMAGE, Image::parse);
    rules.put(TokenType.ITALIC, Pair.parse(SymbolType.ITALIC));
    rules.put(TokenType.LAST_MODIFIED, makeType(SymbolType.LAST_MODIFIED));
    rules.put(TokenType.LINK, Link::parse);
    rules.put(TokenType.LITERAL_START, Literal::parse);
    rules.put(TokenType.META, Line.parse(SymbolType.META));
    rules.put(TokenType.NESTING_START, Nesting::parse);
    rules.put(TokenType.NESTING_PSEUDO_START, Nesting::parse);
    rules.put(TokenType.NEW_LINE, makeType(SymbolType.NEW_LINE));
    rules.put(TokenType.NOTE, Line.parse(SymbolType.NOTE));
    rules.put(TokenType.NUMBERED_LIST, WikiList::parse);
    rules.put(TokenType.PATH, Path::parse);
    rules.put(TokenType.PLAIN_TEXT_TABLE_START, Table::parsePlain);
    rules.put(TokenType.PREFORMAT_START, Preformat::parse);
    rules.put(TokenType.SEE, Keyword.parse(SymbolType.SEE));
    rules.put(TokenType.STRIKE, Pair.parse(SymbolType.STRIKE));
    rules.put(TokenType.STYLE, Style::parse);
    rules.put(TokenType.TABLE_START, Table::parseStandard);
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
