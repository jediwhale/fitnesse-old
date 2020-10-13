package fitnesse.wikitext.parser3;

public class Keyword {
  public static ParseRule parse(SymbolType type) {
    return parser -> {
      Symbol result = new SymbolBranch(type);
      parser.advance();
      result.add(parser.parseCurrent());
      return result;
    };
  }

  public static ParseRule parseWord(SymbolType type) {
    return parser -> {
      if (!parser.peek(1).isWord()) {
        return parser.makeError("Name must be alphanumeric", 1);
      }
      parser.advance();
      Symbol argument = parser.parseCurrent();
      return new SymbolLeaf(type, argument.getContent());
    };
  }
}
