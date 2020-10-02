package fitnesse.wikitext.parser3;

class Include {
  static Symbol parse(Parser parser) {
    parser.advance();
    parser.advance();
    return new Symbol(SymbolType.INCLUDE);
  }

  static String translate(Symbol symbol, Translator translator) {
    return "stuff";
  }
}
