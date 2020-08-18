package fitnesse.wikitext.parser3;

import java.util.function.Function;

public class Matcher {
  public static Matcher make(TokenType type) {
    return new Matcher(content -> content.readString(type.getMatch()) ? type.getMatch() : "", type);
  }

  public static Matcher makeWord(TokenType type) {
    return new Matcher(content -> content.readWord(type.getMatch()), type);
  }

  public static Matcher makeStrings(String[] matches, TokenType type) {
    return new Matcher(strings(matches), type);
  }

  public static Matcher makeBlankSpace(TokenType type) {
    return new Matcher(Content::readBlankSpace, type);
  }

  private static Function<Content, String> strings(String[] matches) {
    return content -> {
      for (String match : matches) {
        if (content.readString(match)) return match;
      }
      return "";
    };
  }

  public Matcher(Function<Content, String> reader, TokenType type) {
    this.reader = reader;
    this.type = type;
  }

  public String read(Content content) { return reader.apply(content);}

  public boolean isType(TokenType type) { return this.type == type; }

  public Token asToken(String content) { return new Token(type, content); }

  private final Function<Content, String> reader;
  private final TokenType type;
}
