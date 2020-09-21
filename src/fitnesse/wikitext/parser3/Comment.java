package fitnesse.wikitext.parser3;

class Comment {

  static Symbol parse(Parser parser) {
    return new Symbol(SymbolType.SOURCE, parser.advance().getContent());
  }
}
