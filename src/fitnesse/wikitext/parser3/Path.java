package fitnesse.wikitext.parser3;

class Path {

  static Symbol parse(Parser parser) {
    Symbol source = new Symbol(SymbolType.SOURCE, parser.peek(0).getContent());
    Token start = parser.advance();
    Symbol result = new Parser(parser).parseList(start).asType(SymbolType.PATH);
    result.addFirst(source);
    return result;
  }

  static String translate(Symbol symbol, Translator translator) {
    return "path"; //todo
  }
}
