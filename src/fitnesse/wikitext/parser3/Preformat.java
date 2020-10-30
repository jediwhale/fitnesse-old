package fitnesse.wikitext.parser3;

class Preformat {

  static Symbol parse(Parser parser) {
    return parser.textType(SymbolType.PREFORMAT).parseList(parser.advance());
  }
}
