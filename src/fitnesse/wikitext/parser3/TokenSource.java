package fitnesse.wikitext.parser3;

import java.util.*;
import java.util.function.Predicate;

class TokenSource {
  TokenSource(Content content, List<TokenType> types) {
    this.content = content;
    results = new LinkedList<>();
    use(types, type -> false);
  }

  void use(List<TokenType> types, Predicate<TokenType> terminator) {
    scanTypes.push(new ScanTypes(types, terminator));
  }

  Token take() {
    while (results.isEmpty()) readResult();
    previous = results.remove();
    return previous;
  }

  void putBack() { results.addFirst(previous); }

  Token peek(int offset) {
    while (results.size() <= offset) readResult();
    return results.get(offset);
  }

  Token getPrevious() {
    return previous;
  }

  private void readResult() {
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

  private void addResult(Token token) {
    if (text.length() > 0) {
      results.add(new Token(TokenType.TEXT, text.toString()));
      text.setLength(0);
    }
    results.add(token);
  }

  private Token previous;
  private final Stack<ScanTypes> scanTypes = new Stack<>();
  private final LinkedList<Token> results;
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