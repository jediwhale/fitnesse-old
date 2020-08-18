package fitnesse.wikitext.parser3;

import static fitnesse.wikitext.parser3.WikiPath.isWikiWordPath;

public class Scanner {
  private static final Matcher[] wikiPageTypes = {

    Matcher.make(TokenType.ALIAS_START),
    Matcher.make(TokenType.ALIAS_MIDDLE),
    Matcher.make(TokenType.ALIAS_END),
    Matcher.makeWord(TokenType.ANCHOR_NAME),
    Matcher.make(TokenType.ANCHOR_REFERENCE),
    Matcher.make(TokenType.BOLD_ITALIC),
    Matcher.make(TokenType.CELL_DELIMITER),
    Matcher.makeWord(TokenType.DEFINE),
    Matcher.makeWord(TokenType.SEE),
    Matcher.make(TokenType.STRIKE),
    Matcher.make(TokenType.STYLE),
    Matcher.makeStrings(new String[]{"http://", "https://"}, TokenType.LINK),
    Matcher.make(TokenType.LITERAL_START),
    Matcher.make(TokenType.LITERAL_END),
    Matcher.make(TokenType.NESTING_START),
    Matcher.make(TokenType.NESTING_END),
    Matcher.makeStrings(new String[]{"\r\n", "\n", "\r"}, TokenType.NEW_LINE),
    Matcher.makeWord(TokenType.NOTE),
    Matcher.makeWord(TokenType.PATH),
    Matcher.make(TokenType.PREFORMAT_START),
    Matcher.make(TokenType.PREFORMAT_END),
    Matcher.make(TokenType.VARIABLE),
    Matcher.makeBlankSpace(TokenType.BLANK_SPACE),

    Matcher.make(TokenType.BOLD),
    Matcher.make(TokenType.BRACE_START),
    Matcher.make(TokenType.BRACE_END),
    Matcher.make(TokenType.BRACKET_START),
    Matcher.make(TokenType.BRACKET_END),
    Matcher.make(TokenType.ITALIC),
    Matcher.make(TokenType.PARENTHESIS_START),
    Matcher.make(TokenType.PARENTHESIS_END)
  };

  private static final Matcher[] literalTypes = { Matcher.make(TokenType.LITERAL_END) };
  private static final Matcher[] preformatTypes = { Matcher.make(TokenType.PREFORMAT_END) };

  public Scanner(String input) {
    content = new Content(input);
  }

  public TokenList scan() {
    result = new TokenList();
    scan(wikiPageTypes);
    add(new Token(TokenType.END));
    return result;
  }

  public void scan(Matcher[] matches) {
    while (content.more()) {
      if (!findMatch(matches)) {
        text.append(content.advance());
      }
    }
  }

  private boolean findMatch(Matcher[] matches) {
    for (Matcher match : matches) {
      String matched = match.read(content);
      if (matched.length() > 0) {
        add(match.asToken(matched));
        if (match.isType(TokenType.LITERAL_START)) {
          scan(literalTypes);
        }
        if (match.isType(TokenType.PREFORMAT_START)) {
          scan(preformatTypes);
        }
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
