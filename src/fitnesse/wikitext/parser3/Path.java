package fitnesse.wikitext.parser3;

class Path {

  static Symbol parse(Parser parser) {
    Symbol source = new Symbol(SymbolType.SOURCE, parser.peek(0).getContent());
    Symbol result = new Parser(parser).parseList(SymbolType.PATH, parser.advance());
    result.addFirst(source);
    return result;
  }

  static String translate(Symbol symbol, Translator translator) {
    return "path"; //todo
  }
}
