package fitnesse.wikitext.parser3;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Predicate;

class TokenSource {
  TokenSource(Content content, List<TokenType> types) {
    this.content = content;
    results = new LinkedList<>();
    use(types, type -> false);
  }

  TokenSource(TokenSource parent, Content content) { //todo: this is used for variable and include content, eventually goes away?
    this.content = content;
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
        if (textOffset < 0) textOffset = content.getCurrent();
        text.append(content.advance());
      }
    } else {
      addResult(new Token(TokenType.END));
    }
  }

  private void addResult(Token token) {
    if (text.length() > 0) {
      results.add(new Token(text.toString(), textOffset));
      text.setLength(0);
      textOffset = -1;
    }
    results.add(token);
  }

  private Token previous;
  private final Stack<ScanTypes> scanTypes = new Stack<>();
  private final LinkedList<Token> results;
  private final Content content;
  private final StringBuilder text = new StringBuilder();
  private int textOffset = -1;

  private static class ScanTypes {
    ScanTypes(List<TokenType> types, Predicate<TokenType> terminator) {
      this.types = types;
      this.terminator = terminator;
    }

    Optional<Token> findMatch(Content content) {
      for (TokenType matchType : types) { //todo: do quicker than linear search
        Optional<Token> matchToken = matchType.read(content);
        if (matchToken.isPresent()) return matchToken;
      }
      return Optional.empty();
    }

    boolean isTerminated(TokenType type) { return terminator.test(type);}

    final List<TokenType> types;
    private final Predicate<TokenType> terminator;
  }
}
