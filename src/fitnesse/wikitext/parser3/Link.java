package fitnesse.wikitext.parser3;

public class Link {
  public static Symbol parse(Parser parser) {
    Symbol result = new BranchSymbol(SymbolType.LINK, parser.peek(0).getContent());
    parser.advance();
    result.add(parser.textType(SymbolType.TEXT).parseCurrent());
    return result;
  }
}
