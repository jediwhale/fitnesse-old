package fitnesse.wikitext.parser3;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

class Scanner {
  Scanner() {
    this(TokenTypes.WIKI_PAGE_TYPES);
  }

  Scanner(List<TokenType> types) {
    this(types, type -> false);
  }

  Scanner(TokenType match) {
    this(Collections.singletonList(match), type -> type == match);
  }

  Scanner(List<TokenType> matches, Predicate<TokenType> terminator) {
    this.matches = matches;
    this.terminator = terminator;
  }

  TokenList scan(String input) {
    TokenList result = new TokenList();
    scan(new Content(input), result);
    add(new Token(TokenType.END), result);
    return result;
  }

  void scan(Content content, TokenList tokens) {
    while (content.more() && !tokens.hasLast(terminator)) {
      if (!findMatch(content, tokens)) {
        text.append(content.advance());
      }
    }
    tokens.addText(text);
  }

  static void scanHashTable(Content content, TokenList tokens) {
    new Scanner(TokenTypes.HASH_TABLE_TYPES, type -> type == TokenType.BRACE_END).scan(content, tokens);
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
    tokens.addText(text);
    tokens.add(newToken);
  }

  private final List<TokenType> matches;
  private final StringBuilder text = new StringBuilder();
  private final Predicate<TokenType> terminator;
}
