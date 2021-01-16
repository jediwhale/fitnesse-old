package fitnesse.wikitext.parser3;

public class TokenType {

  public static final TokenType END = new TokenType("End");
  public static final TokenType TEXT = new TokenType("Text");

  public TokenType(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public TokenType(String name) {
    this(name, name);
  }

  public String getDescription() { return description; }
  public String toString() { return name; }

  private final String description;
  private final String name;
}
