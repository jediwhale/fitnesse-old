package fitnesse.wikitext.parser3;

import static fitnesse.wikitext.parser3.WikiPath.isWikiWordPath;

public class Scanner {
  private static final TokenType[] wikiPageTypes = {
    TokenType.ALIAS_START,
    TokenType.ALIAS_MIDDLE,
    TokenType.ALIAS_END,
    TokenType.ANCHOR_NAME,
    TokenType.ANCHOR_REFERENCE,
    TokenType.BOLD_ITALIC,
    TokenType.CELL_DELIMITER,
    TokenType.CENTER_LINE,
    TokenType.DEFINE,
    TokenType.HEADINGS,
    TokenType.IMAGE,
    TokenType.INCLUDE,
    TokenType.LINK,
    TokenType.LITERAL_START,
    TokenType.LITERAL_END,
    TokenType.META,
    TokenType.NESTING_START,
    TokenType.NESTING_END,
    TokenType.NEW_LINE,
    TokenType.NOTE,
    TokenType.PATH,
    TokenType.PREFORMAT_START,
    TokenType.PREFORMAT_END,
    TokenType.SEE,
    TokenType.STRIKE,
    TokenType.STYLE,
    TokenType.VARIABLE,

    TokenType.BLANK_SPACE,
    TokenType.BOLD,
    TokenType.BRACE_START,
    TokenType.BRACE_END,
    TokenType.BRACKET_START,
    TokenType.BRACKET_END,
    TokenType.ITALIC,
    TokenType.PARENTHESIS_START,
    TokenType.PARENTHESIS_END
  };

  public Scanner(String input) {
    content = new Content(input);
  }

  public TokenList scan() {
    result = new TokenList();
    scan(wikiPageTypes);
    add(new Token(TokenType.END));
    return result;
  }

  public void scan(TokenType[] matches) {
    while (content.more()) {
      if (!findMatch(matches)) {
        text.append(content.advance());
      }
    }
  }

  private boolean findMatch(TokenType[] matches) {
    for (TokenType match : matches) {
      String matched = match.read(content);
      if (matched.length() > 0) {
        add(match.asToken(matched));
        return true;
      }
    }
    return false;
  }

  private void add(Token newToken) {
    if (text.length() > 0) {
      addText(text.toString());
      text.setLength(0);
    }
    result.add(newToken);
  }

  private void addText(String input) {
    if (isWikiWordPath(input)) {
      result.add(new Token(TokenType.WIKI_PATH, input));
    } else {
      result.add(new Token(TokenType.TEXT, input));
    }
  }

  private final Content content;
  private final StringBuilder text = new StringBuilder();
  private TokenList result;
}
