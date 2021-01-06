package fitnesse.wikitext.parser3;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Predicate;

class TokenSource {
  TokenSource(Content content, TokenTypes types) {
    this.content = content;
    results = new LinkedList<>();
    use(types, type -> false);
  }

  TokenSource(TokenSource parent, Content content) { //todo: this is used for variable and include content, eventually goes away?
    this.content = content;
    results = new LinkedList<>();
    use(parent.scanTypes.lastElement().types, type -> false);
  }

  TokenSource(TokenSource parent, TokenTypes types) {
    this.content = parent.content;
    results = new LinkedList<>();
    use(types, type -> false);
  }

  void putBack() { results.addFirst(previous); }
  Token getPrevious() { return previous; }

  void use(TokenTypes types, Predicate<TokenType> terminator) {
    scanTypes.push(new ScanTypes(types, terminator));
  }

  void popTypes() { scanTypes.pop(); }

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
        collectText();
      }
    } else {
      addResult(new Token(TokenType.END));
    }
  }

  private void collectText() {
    int current = content.getCurrent();
    char character = content.advance();

    // the pseudo-nesting characters are used for compatibility with parser v2 to handle variables inside table cells
    // if they haven't been used by now, they are discarded
    if (character == Nesting.START || character == Nesting.END) return;

    if (textOffset < 0) textOffset = current;
    text.append(character);
  }

  private void addResult(Token token) {
    if (text.length() > 0) {
      String textString = text.toString();
      //todo? check list of keyword tokentypes, if none, tokentype.text
      results.add(new Token(scanTypes.peek().findKeywordType(textString).orElse(TokenType.TEXT),textString, textOffset));
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
    ScanTypes(TokenTypes types, Predicate<TokenType> terminator) {
      this.types = types;
      this.terminator = terminator;
    }

    Optional<Token> findMatch(Content content) {
      for (TokenType matchType : types.getDelimiters()) { //todo: do quicker than linear search
        Optional<Token> matchToken = matchType.read(content);
        if (matchToken.isPresent()) return matchToken;
      }
      return Optional.empty();
    }

    Optional<TokenType> findKeywordType(String text) {
      for (TokenType keywordType: types.getKeywords()) {
        if (keywordType.getMatch().equals(text)) return Optional.of(keywordType);
      }
      return Optional.empty();
    }

    boolean isTerminated(TokenType type) { return terminator.test(type);}

    //todo? make this a class, provides list of types and can check text for keywords
    //todo? maybe also check text for wikiword, etc
    final TokenTypes types;
    private final Predicate<TokenType> terminator;
  }
}
