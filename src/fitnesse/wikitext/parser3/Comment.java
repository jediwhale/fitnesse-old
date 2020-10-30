package fitnesse.wikitext.parser3;

class Comment {
  static Symbol parse(Parser parser) {
    parser.advance();
    return new LeafSymbol(SymbolType.TEXT, "");
  }
}
