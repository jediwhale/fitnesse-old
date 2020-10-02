package fitnesse.wikitext.parser3;

import java.util.function.Predicate;

public class Terminator {
  public Terminator(TokenType tokenType) {
    this(type -> type == tokenType, tokenType.getMatch());
  }

  public Terminator(Predicate<TokenType> matcher, String name) {
    this.matcher = matcher;
    this.name = name;
  }

  public boolean matches(TokenType candidate) {
    return matcher.test(candidate);
  }

  public String getName() {
    return name;
  }

  private final Predicate<TokenType> matcher;
  private final String name;
}
