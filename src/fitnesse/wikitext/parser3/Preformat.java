package fitnesse.wikitext.parser3;

public class Preformat {
  public  static void scan(Content content, TokenList tokens) {
    new Scanner(TokenType.PREFORMAT_END)
      .scan(content, tokens);
  }

  public static Symbol parse(Parser parser) {
    parser.advance();
    Symbol result = parser.parseList(parser.peek(-1));
    if (result.hasError()) return result;
    result.addFirst(Symbol.source(TokenType.PREFORMAT_START));
    result.add(Symbol.source(TokenType.PREFORMAT_END));
    return result;
  }
}
