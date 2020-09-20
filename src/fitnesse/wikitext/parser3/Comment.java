package fitnesse.wikitext.parser3;

class Comment {

  static Symbol parse(Parser parser) {
    parser.advance();
    return new Symbol(SymbolType.SOURCE, parser.peek(-1).getContent());
  }
}
