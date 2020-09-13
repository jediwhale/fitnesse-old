package fitnesse.wikitext.parser3;

public class Preformat {
  public  static void scan(Content content, TokenList tokens) {
    new Scanner(TokenType.PREFORMAT_END, text -> TokenType.TEXT)
      .scan(content, tokens);
  }

  public static Symbol parse(Parser parser) {
    parser.advance();
    Symbol result = parser.parseList(parser.peek(-1));
    if (result.hasError()) return result;
    result.addFirst(Symbol.inputText(TokenType.PREFORMAT_START));
    result.add(Symbol.inputText(TokenType.PREFORMAT_END));
    return result;
  }
}
