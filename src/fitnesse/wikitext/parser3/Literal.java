package fitnesse.wikitext.parser3;

public class Literal {
  public static Symbol parse(Parser parser) {
    parser.advance();
    Symbol result = parser.parseList(parser.peek(-1));
    if (result.hasError()) return result;
    result.addFirst(new Symbol(SymbolType.INPUT_TEXT, TokenType.LITERAL_START.getMatch()));
    result.add(new Symbol(SymbolType.INPUT_TEXT, TokenType.LITERAL_END.getMatch()));
    return result;
  }
}
