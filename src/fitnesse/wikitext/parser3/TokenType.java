package fitnesse.wikitext.parser3;

import java.util.function.BiConsumer;

public class TokenType {

  public static final TokenType END = new TokenType("End");
  public static final TokenType TEXT = new TokenType("Text");

  public TokenType(String name, String match) {
    this.match = match;
    this.name = name;
  }

  public TokenType(String name) {
    this.name = name;
    this.match = name;
  }

  public String getMatch() { return match; }
  public String toString() { return name; }

  public void useScan(Token token, TokenSource source) { useScan.accept(token, source); }

  protected BiConsumer<Token, TokenSource> useScan = (t, s) -> {};

  private final String match;
  private final String name;
}
