package fitnesse.wikitext.parser3;

public class Pair {
  public static ParseRule parse(SymbolType symbolType) {
    return parser -> parser.parseList(symbolType, parser.advance());
  }
}
