package fitnesse.wikitext.parser3;

class Comment {

  static Symbol parse(Parser parser) {
    return new SymbolLeaf(SymbolType.SOURCE, parser.advance().getContent());
  }
}
