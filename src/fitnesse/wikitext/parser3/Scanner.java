package fitnesse.wikitext.parser3;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static fitnesse.wikitext.parser3.WikiPath.isWikiWordPath;

public class Scanner {
  public Scanner() {
    this(TokenTypes.WIKI_PAGE_TYPES);
  }

  public Scanner(List<TokenType> types) {
    this(types, type -> false, Scanner::wikiTextType);
  }

  public Scanner(TokenType match, Function<String, TokenType> textType) {
    this(Collections.singletonList(match), type -> type == match, textType);
  }

  private Scanner(List<TokenType> matches, Predicate<TokenType> terminator, Function<String, TokenType> textType) {
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

  public  static void scanHashTable(Content content, TokenList tokens) {
    new Scanner(TokenTypes.HASH_TABLE_TYPES, type -> type == TokenType.BRACE_END, Scanner::wikiTextType).scan(content, tokens);
  }

  private boolean findMatch(Content content, TokenList tokens) {
    for (TokenType match : matches) {
      Optional<String> matched = match.read(content);
      if (matched.isPresent()) {
        add(match.asToken(matched.get()), tokens);
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
