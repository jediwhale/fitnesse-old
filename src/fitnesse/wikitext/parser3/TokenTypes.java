package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.Arrays;

public class TokenTypes {
  public static final ArrayList<TokenType> CORE_TYPES = new ArrayList<>(Arrays.asList(
    TokenType.ALIAS_START,
    TokenType.ALIAS_MIDDLE,
    TokenType.ALIAS_END,
    TokenType.ANCHOR_NAME,
    TokenType.ANCHOR_REFERENCE,
    TokenType.BOLD_ITALIC,
    TokenType.BULLET_LIST,
    TokenType.CENTER,
    TokenType.COLLAPSIBLE_END,
    TokenType.COLLAPSIBLE_START,
    TokenType.COMMENT,
    TokenType.CONTENTS,
    TokenType.EXPRESSION_START,
    TokenType.EXPRESSION_END,
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
    TokenType.META,
    TokenType.NESTING_START,
    TokenType.NESTING_END,
    TokenType.NOTE,
    TokenType.NUMBERED_LIST,
    TokenType.PATH,
    TokenType.PLAIN_TEXT_TABLE_START,
    TokenType.PLAIN_TEXT_TABLE_END,
    TokenType.PREFORMAT_START,
    TokenType.PREFORMAT_END,
    TokenType.SEE,
    TokenType.STYLE,
    TokenType.TABLE_START,
    TokenType.TABLE_END,
    TokenType.TODAY,

    TokenType.LITERAL_END,
    TokenType.NEW_LINE,
    TokenType.BLANK_SPACE,
    TokenType.BOLD,
    TokenType.BRACE_START,
    TokenType.BRACE_END,
    TokenType.BRACKET_START,
    TokenType.BRACKET_END,
    TokenType.CELL_DELIMITER,
    TokenType.ITALIC,
    TokenType.PARENTHESIS_START,
    TokenType.PARENTHESIS_END,
    TokenType.STRIKE
  ));

  public static final ArrayList<TokenType> WIKI_PAGE_TYPES = new ArrayList<>(CORE_TYPES);
  static {
    WIKI_PAGE_TYPES.add(0, TokenType.VARIABLE_VALUE); //must be first
    WIKI_PAGE_TYPES.add(TokenType.DEFINE);
  }

  public static final ArrayList<TokenType> DEFINE_TYPES = new ArrayList<>(CORE_TYPES);
  static {
    DEFINE_TYPES.add(0, TokenType.VARIABLE_TOKEN); //must be first
    DEFINE_TYPES.add(TokenType.DEFINE_NESTED);
  }

  public static final ArrayList<TokenType> HASH_TABLE_TYPES = new ArrayList<>(WIKI_PAGE_TYPES);
  static {
    HASH_TABLE_TYPES.add(TokenType.COLON);
    HASH_TABLE_TYPES.add(TokenType.COMMA);
  }

  public static final ArrayList<TokenType> STANDARD_TABLE_TYPES = new ArrayList<>(WIKI_PAGE_TYPES);
  static {
    STANDARD_TABLE_TYPES.add(TokenType.NESTING_PSEUDO_START);
    STANDARD_TABLE_TYPES.add(TokenType.NESTING_PSEUDO_END);
  }

  public static final ArrayList<TokenType> NO_LINK_TABLE_TYPES = new ArrayList<>(STANDARD_TABLE_TYPES);
  static {
    NO_LINK_TABLE_TYPES.remove(TokenType.LINK);
  }

  static final ArrayList<TokenType> LITERAL_TABLE_TYPES = new ArrayList<>(Arrays.asList(
    TokenType.VARIABLE_VALUE, // must be first
    TokenType.EXPRESSION_START,
    TokenType.EXPRESSION_END,
    TokenType.LITERAL_START,
    TokenType.LITERAL_END,
    TokenType.BRACE_END,
    TokenType.NEW_LINE,
    TokenType.TABLE_END,
    TokenType.CELL_DELIMITER,
    TokenType.NESTING_PSEUDO_START,
    TokenType.NESTING_PSEUDO_END
  ));

  public static final ArrayList<TokenType> REFACTORING_TYPES = new ArrayList<>(Arrays.asList(
    TokenType.ALIAS_END,
    TokenType.ALIAS_MIDDLE,
    TokenType.ALIAS_START,
    TokenType.COMMENT,
    TokenType.IMAGE,
    TokenType.LINK,
    TokenType.LITERAL_START,
    TokenType.NEW_LINE,
    TokenType.PATH,
    TokenType.PREFORMAT_START,

    TokenType.BLANK_SPACE,
    TokenType.BRACKET_END,
    TokenType.BRACKET_START
  ));

  public static final ArrayList<TokenType> VARIABLE_DEFINITION_TYPES = new ArrayList<>(Arrays.asList(
    TokenType.VARIABLE_VALUE, // must be first
    TokenType.COMMENT,
    TokenType.DEFINE,
    TokenType.INCLUDE,
    TokenType.LITERAL_START,
    TokenType.NEW_LINE,
    TokenType.PREFORMAT_START,
    TokenType.PREFORMAT_END,

    TokenType.BLANK_SPACE,
    TokenType.BRACE_END
  ));
}
