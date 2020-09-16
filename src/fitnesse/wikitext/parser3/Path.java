package fitnesse.wikitext.parser3;

class Path {

  static Symbol parse(Parser parser) {
    Symbol source = new Symbol(SymbolType.SOURCE, parser.peek(0).getContent());
    parser.advance();
    Symbol result = new Parser(parser).parseList(parser.peek(-1)).asType(SymbolType.PATH);
    result.addFirst(source);
    return result;
  }

  static String translate(Symbol symbol, Translator translator) {
    return "path"; //todo
  }
}
