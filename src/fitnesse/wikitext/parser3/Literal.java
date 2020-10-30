package fitnesse.wikitext.parser3;

class Literal {
  static Symbol parse(Parser parser) {
    return parser.textType(SymbolType.LITERAL).parseList(parser.advance());
  }

  static String translate(Symbol symbol, Translator translator) {
    return symbol.getContent() + symbol.translateChildren(translator);
  }
}
