package fitnesse.wikitext.parser3;

class Line {
  static ParseRule parse(SymbolType type) {
    return parser -> {
      Symbol result = new BranchSymbol(type);
      parser.advance();
      parser.advance(); //todo: check blank
      result.add(parser.parseListInParent(SymbolType.LIST, Terminator.END_LINE));
      return result;
    };
  }
}
