package fitnesse.wikitext.parser3;

public class Comment {
  public  static void scan(Content content, TokenList tokens) {
    new Scanner(TokenType.NEW_LINE, text -> TokenType.TEXT)
      .scan(content, tokens);
  }

  public static Symbol parse(Parser parser) {
    parser.advance();
    String content = parser.peek(-1).getContent() + parser.peek(0).getContent();
    parser.advance();
    if (parser.peek(0).isType(TokenType.NEW_LINE)) {
      content += parser.peek(0).getContent();
      parser.advance();
    }
    return new Symbol(SymbolType.SOURCE, content);
  }
}
