package fitnesse.wikitext.parser3;

import java.util.function.Predicate;

public class Terminator {
  public Terminator(TokenType tokenType) {
    this(token -> token.isType(tokenType), tokenType.getMatch());
  }

  public Terminator(Predicate<Token> matcher, String name) {
    this.matcher = matcher;
    this.name = name;
  }

  public boolean matches(Token candidate) {
    return matcher.test(candidate);
  }

  public String getName() {
    return name;
  }

  private final Predicate<Token> matcher;
  private final String name;
}
