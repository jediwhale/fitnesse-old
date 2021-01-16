package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class TokenTypes {
  private static final TokenTypes CORE_TYPES = new TokenTypes(
    Arrays.asList(
      DelimiterType.ALIAS_START,
      DelimiterType.ALIAS_MIDDLE,
      DelimiterType.ALIAS_END,
      DelimiterType.ANCHOR_REFERENCE,
      DelimiterType.BOLD_ITALIC,
      DelimiterType.BULLET_LIST,
      DelimiterType.COLLAPSIBLE_END,
      DelimiterType.COLLAPSIBLE_START,
      DelimiterType.COMMENT,
      DelimiterType.EXPRESSION_START,
      DelimiterType.EXPRESSION_END,
      DelimiterType.HASH_TABLE,
      DelimiterType.HORIZONTAL_RULE,
      DelimiterType.LINK,
      DelimiterType.LITERAL_START,
      DelimiterType.NESTING_START,
      DelimiterType.NESTING_END,
      DelimiterType.NUMBERED_LIST,
      DelimiterType.PLAIN_TEXT_TABLE_START,
      DelimiterType.PLAIN_TEXT_TABLE_END,
      DelimiterType.PREFORMAT_START,
      DelimiterType.PREFORMAT_END,
      DelimiterType.STYLE,
      DelimiterType.TABLE_START,
      DelimiterType.TABLE_END,

      DelimiterType.LITERAL_END,
      DelimiterType.NEW_LINE,
      DelimiterType.BLANK_SPACE,
      DelimiterType.BOLD,
      DelimiterType.BRACE_START,
      DelimiterType.BRACE_END,
      DelimiterType.BRACKET_START,
      DelimiterType.BRACKET_END,
      DelimiterType.CELL_DELIMITER,
      DelimiterType.ITALIC,
      DelimiterType.PARENTHESIS_START,
      DelimiterType.PARENTHESIS_END,
      DelimiterType.STRIKE
    ),
    Arrays.asList(
      KeywordType.ANCHOR_NAME,
      KeywordType.CONTENTS,
      KeywordType.CENTER,
      KeywordType.HEADER,
      KeywordType.HEADINGS,
      KeywordType.HELP,
      KeywordType.IMAGE,
      KeywordType.INCLUDE,
      KeywordType.LAST_MODIFIED,
      KeywordType.META,
      KeywordType.NOTE,
      KeywordType.PATH,
      KeywordType.SEE,
      KeywordType.TODAY
    ));

  static final TokenTypes WIKI_PAGE_TYPES = new TokenTypes(CORE_TYPES)
    .addFirst(DelimiterType.VARIABLE_VALUE) //must be first
    .addKeyword(KeywordType.DEFINE);

  static final TokenTypes DEFINE_TYPES = new TokenTypes(CORE_TYPES)
    .addFirst(DelimiterType.VARIABLE_TOKEN) //must be first
    .addKeyword(KeywordType.DEFINE_NESTED);

  static final TokenTypes HASH_TABLE_TYPES = new TokenTypes(WIKI_PAGE_TYPES)
    .add(DelimiterType.COLON)
    .add(DelimiterType.COMMA);

  static final TokenTypes STANDARD_TABLE_TYPES = new TokenTypes(WIKI_PAGE_TYPES)
    .add(DelimiterType.NESTING_PSEUDO_START)
    .add(DelimiterType.NESTING_PSEUDO_END);

  static final TokenTypes NO_LINK_TABLE_TYPES = new TokenTypes(STANDARD_TABLE_TYPES)
    .remove(DelimiterType.LINK);

  TokenTypes(List<TokenType> delimiters) {
    this(delimiters, Collections.emptyList());
  }

  TokenTypes(List<TokenType> delimiters, List<KeywordType> keywords) {
    this.delimiters = delimiters;
    this.keywords = keywords;
  }

  TokenTypes(TokenTypes other) {
    delimiters = new ArrayList<>(other.delimiters);
    keywords = new ArrayList<>(other.keywords);
  }

  //todo: replace with function to search delimiters
  List<TokenType> getDelimiters() {
    return delimiters;
  }

  //todo: replace with function to search keywords
  List<KeywordType> getKeywords() {
    return keywords;
  }

  private TokenTypes add(TokenType type) {
    delimiters.add(type);
    return this;
  }

  private TokenTypes addFirst(TokenType type) {
    delimiters.add(0, type);
    return this;
  }

  private TokenTypes remove(TokenType type) {
    delimiters.remove(type);
    return this;
  }

  private TokenTypes addKeyword(KeywordType type) {
    keywords.add(type);
    return this;
  }

  private final List<TokenType> delimiters;
  private final List<KeywordType> keywords;
}
