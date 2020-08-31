package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static fitnesse.wikitext.parser3.WikiPath.isWikiWordPath;

public class Scanner {
  private static final ArrayList<TokenType> WIKI_PAGE_TYPES = new ArrayList<>(Arrays.asList(
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

  private static final ArrayList<TokenType> LITERAL_TYPES = new ArrayList<>(Collections.singletonList(
    TokenType.LITERAL_END
  ));

  private static final ArrayList<TokenType> HASH_TABLE_TYPES = new ArrayList<>(WIKI_PAGE_TYPES);
  static {
    HASH_TABLE_TYPES.add(TokenType.COLON);
    HASH_TABLE_TYPES.add(TokenType.COMMA);
  }

  public Scanner() {
    this(WIKI_PAGE_TYPES, type -> false, Scanner::wikiTextType);
  }

  public Scanner(List<TokenType> matches, Predicate<TokenType> terminator, Function<String, TokenType> textType) {
    this.matches = matches;
    this.textType = textType;
    this.terminator = terminator;
  }

  public TokenList scan(String input) {
    TokenList result = new TokenList();
    scan(new Content(input), result);
    add(new Token(TokenType.END), result);
    return result;
  }

  public void scan(Content content, TokenList tokens) {
    while (content.more() && !tokens.hasLast(terminator)) {
      if (!findMatch(content, tokens)) {
        text.append(content.advance());
      }
    }
    tokens.addText(text, textType);
  }

  public  static void scanLiteral(Content content, TokenList tokens) {
    new Scanner(LITERAL_TYPES, type -> type == TokenType.LITERAL_END, text -> TokenType.TEXT).scan(content, tokens);
  }

  public  static void scanHashTable(Content content, TokenList tokens) {
    new Scanner(HASH_TABLE_TYPES, type -> type == TokenType.BRACE_END, Scanner::wikiTextType).scan(content, tokens);
  }

  private boolean findMatch(Content content, TokenList tokens) {
    for (TokenType match : matches) {
      String matched = match.read(content);
      if (matched.length() > 0) {
        add(match.asToken(matched), tokens);
        match.scan(content, tokens);
        return true;
      }
    }
    return false;
  }

  private void add(Token newToken, TokenList tokens) {
    tokens.addText(text, textType);
    tokens.add(newToken);
  }

  private static TokenType wikiTextType(String input) {
    return isWikiWordPath(input) ? TokenType.WIKI_PATH : TokenType.TEXT;
  }

  private final List<TokenType> matches;
  private final Function<String, TokenType> textType;
  private final StringBuilder text = new StringBuilder();
  private final Predicate<TokenType> terminator;
}
