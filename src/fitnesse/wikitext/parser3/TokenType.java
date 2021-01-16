package fitnesse.wikitext.parser3;

import java.util.Optional;
import java.util.function.BiConsumer;

import static fitnesse.wikitext.parser3.MatchContent.*;

public class TokenType {

  public static final TokenType END = new TokenType("End");
  public static final TokenType TEXT = new TokenType("Text");

  public TokenType(String name, String match) {
    this.match = match;
    this.name = name;
    matcher = text(match);
  }

  public TokenType(String name) {
    this.name = name;
    this.match = name;
    matcher = content -> Optional.empty();
  }

  public String getMatch() { return match; }
  public String toString() { return name; }

  public Optional<Token> read(Content content) {
    Optional<String> result = matcher.check(content);
    if (result.isPresent() && isStart) content.setStartLine();
    return result.map(s -> new Token(this, s));
  }

  public void useScan(Token token, TokenSource source) { useScan.accept(token, source); }

  protected BiConsumer<Token, TokenSource> useScan = (t, s) -> {};
  protected MatchContent matcher;
  protected boolean isStart = false;

  private final String match;
  private final String name;
}
