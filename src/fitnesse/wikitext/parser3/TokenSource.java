package fitnesse.wikitext.parser3;

import java.util.*;
import java.util.function.Predicate;

class TokenSource {
  TokenSource(Content content) {
    this.content = content;
    results = new LinkedList<>();
  }

  void use(List<TokenType> types) {
    use(types, type -> false);
  }

  void use(List<TokenType> types, Predicate<TokenType> terminator) {
    scanTypes.push(new ScanTypes(types, terminator));
  }

  Token next() {
    while (results.isEmpty()) {
      if (content.more()) {
        Optional<Token> token = scanTypes.peek().findMatch(content);
        if (token.isPresent()) {
          addResult(token.get());
          token.get().getType().useScan(this);
          if (scanTypes.peek().isTerminated(token.get().getType())) {
            scanTypes.pop();
          }
        }
        else {
          text.append(content.advance());
        }
      } else {
        addResult(new Token(TokenType.END));
      }
    }
    return results.remove();
  }


  private void addResult(Token token) {
    if (text.length() > 0) {
      results.add(new Token(TokenType.TEXT, text.toString()));
      text.setLength(0);
    }
    results.add(token);
  }

  private final Stack<ScanTypes> scanTypes = new Stack<>();
  private final Queue<Token> results;
  private final Content content;
  private final StringBuilder text = new StringBuilder();

  private static class ScanTypes {
    ScanTypes(List<TokenType> types, Predicate<TokenType> terminator) {
      this.types = types;
      this.terminator = terminator;
    }

    Optional<Token> findMatch(Content content) {
      for (TokenType matchType : types) {
        Optional<String> matchString = matchType.read(content);
        if (matchString.isPresent()) {
          return Optional.of(matchType.asToken(matchString.get()));
        }
      }
      return Optional.empty();
    }

    boolean isTerminated(TokenType type) { return terminator.test(type);}

    private final List<TokenType> types;
    private final Predicate<TokenType> terminator;
  }
}
