package fitnesse.wikitext.parser3;

class Literal {
  static Symbol parse(Parser parser) {
    Token start = parser.advance();
    Symbol result = parser.textType(SymbolType.LITERAL).parseList(start);
    if (result.hasError()) return result;
    result.addFirst(Symbol.source(TokenType.LITERAL_START));
    result.add(Symbol.source(TokenType.LITERAL_END));
    return result;
  }

  static String translate(Symbol symbol, Translator translator) {
    return symbol.getContent() + symbol.translateChildren(translator);
  }
}
