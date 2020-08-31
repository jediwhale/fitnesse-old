package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TokenList {
  public void add(Token token) {
    tokens.add(token);
  }

  public void move(int movement) { current += movement; }

  public Token peek(int offset) {
    return current + offset < tokens.size() ? tokens.get(current + offset) : endToken;
  }

  public boolean hasLast(Predicate<TokenType> terminator) {
    return tokens.size() > 0 && terminator.test(tokens.get(tokens.size() - 1).getType());
  }

  public void addText(StringBuilder text, Function<String, TokenType> textType) {
    if (text.length() == 0) return;
    String input = text.toString();
    tokens.add(new Token(textType.apply(input), input));
    text.setLength(0);
  }

  public void consumeToTerminator(Terminator terminator, Consumer<Token> action, Consumer<String> error) {
    consume(token -> {
      if (terminator.matches(token)) return true;
      if (token.isType(TokenType.END)) {
        error.accept("Missing terminator: " + terminator.getName());
        return true;
      }
      action.accept(token);
      return false;
    });
  }

  public void consume(Predicate<Token> terminate) {
    while (current < tokens.size()) {
      if (terminate.test(peek(0))) break;
    }
  }

  public String toString() {
    StringBuilder result = new StringBuilder();
    for (Token token : tokens) {
      if (result.length() > 0) result.append(",");
      result.append(token.toString());
    }
    return result.toString();
  }

  private static final Token endToken = new Token(TokenType.END);
  private final List<Token> tokens = new ArrayList<>();
  private int current = 0;
}
