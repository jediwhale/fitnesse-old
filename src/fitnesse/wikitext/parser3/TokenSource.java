package fitnesse.wikitext.parser3;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

class TokenSource {
  TokenSource(Content content) {
    this.content = content;
    results = new LinkedList<>();
  }

  void use(List<TokenType> types) {
    this.types = types;
  }

  Token next() {
    while (results.isEmpty()) {
      if (content.more()) {
        if (!findMatch()) text.append(content.advance());
      } else {
        addResult(new Token(TokenType.END));
      }
    }
    return results.remove();
  }

  private boolean findMatch() {
    for (TokenType matchType : types) {
      Optional<String> matchString = matchType.read(content);
      if (matchString.isPresent()) {
        addResult(matchType.asToken(matchString.get()));
        return true;
      }
    }
    return false;
  }

  void addResult(Token token) {
    if (text.length() > 0) {
      results.add(new Token(TokenType.TEXT, text.toString()));
      text.setLength(0);
    }
    results.add(token);
  }

  List<TokenType> types;
  final Queue<Token> results;
  final Content content;
  final StringBuilder text = new StringBuilder();
}
