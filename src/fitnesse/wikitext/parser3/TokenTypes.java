package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TokenTypes {

  public static final ArrayList<TokenType> WIKI_PAGE_TYPES = new ArrayList<>(Arrays.asList(
    TokenType.ALIAS_START,
    TokenType.ALIAS_MIDDLE,
    TokenType.ALIAS_END,
    TokenType.ANCHOR_NAME,
    TokenType.ANCHOR_REFERENCE,
    TokenType.BOLD_ITALIC,
    TokenType.CELL_DELIMITER,
    TokenType.CENTER_LINE,
    TokenType.COLLAPSIBLE_END,
    TokenType.COLLAPSIBLE_START,
    TokenType.CONTENTS,
    TokenType.DEFINE,
    TokenType.HASH_TABLE,
    TokenType.HEADER,
    TokenType.HEADINGS,
    TokenType.HELP,
    TokenType.HORIZONTAL_RULE,
    TokenType.IMAGE,
    TokenType.INCLUDE,
    TokenType.LAST_MODIFIED,
    TokenType.LINK,
    TokenType.LITERAL_START,
    TokenType.LITERAL_END,
    TokenType.META,
    TokenType.NESTING_START,
    TokenType.NESTING_END,
    TokenType.NEW_LINE,
    TokenType.NOTE,
    TokenType.PATH,
    TokenType.PLAIN_TEXT_TABLE_START,
    TokenType.PLAIN_TEXT_TABLE_END,
    TokenType.PREFORMAT_START,
    TokenType.PREFORMAT_END,
    TokenType.SEE,
    TokenType.STYLE,
    TokenType.TODAY,
    TokenType.VARIABLE,

    TokenType.BLANK_SPACE,
    TokenType.BOLD,
    TokenType.BRACE_START,
    TokenType.BRACE_END,
    TokenType.BRACKET_START,
    TokenType.BRACKET_END,
    TokenType.ITALIC,
    TokenType.PARENTHESIS_START,
    TokenType.PARENTHESIS_END,
    TokenType.STRIKE
  ));

  public static final ArrayList<TokenType> LITERAL_TYPES = new ArrayList<>(Collections.singletonList(
    TokenType.LITERAL_END
  ));

  public static final ArrayList<TokenType> HASH_TABLE_TYPES = new ArrayList<>(WIKI_PAGE_TYPES);
  static {
    HASH_TABLE_TYPES.add(TokenType.COLON);
    HASH_TABLE_TYPES.add(TokenType.COMMA);
  }

  public static final ArrayList<TokenType> VARIABLE_DEFINITION_TYPES = new ArrayList<>(Arrays.asList(
    //TokenType.COMMENT
    TokenType.DEFINE,
    TokenType.INCLUDE,
    TokenType.LITERAL_START,
    TokenType.NEW_LINE,
    TokenType.PREFORMAT_START,
    TokenType.PREFORMAT_END,
    TokenType.VARIABLE,

    TokenType.BLANK_SPACE,
    TokenType.BRACE_END
  ));
}
