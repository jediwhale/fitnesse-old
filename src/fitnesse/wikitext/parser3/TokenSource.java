package fitnesse.wikitext.parser3;

import java.util.*;
import java.util.function.Predicate;

class TokenSource {
  TokenSource(String input, List<TokenType> types) {
    content = new Content(input);
    results = new LinkedList<>();
    use(types, type -> false);
  }

  TokenSource(TokenSource parent, String input) {
    content = new Content(input);
    results = new LinkedList<>();
    use(parent.scanTypes.lastElement().types, type -> false);
  }

  void putBack() { results.addFirst(previous); }
  Token getPrevious() { return previous; }

  void use(List<TokenType> types, Predicate<TokenType> terminator) {
    scanTypes.push(new ScanTypes(types, terminator));
  }

  Token take() {
    while (results.isEmpty()) readResult();
    previous = results.remove();
    return previous;
  }

  Token peek(int offset) {
    while (results.size() <= offset) readResult();
    return results.get(offset);
  }

  private void readResult() {
    if (content.more()) {
      Optional<Token> token = scanTypes.peek().findMatch(content);
      if (token.isPresent()) {
        addResult(token.get());
        token.get().getType().useScan(token.get(), this);
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

    final List<TokenType> types;
    private final Predicate<TokenType> terminator;
  }
}
