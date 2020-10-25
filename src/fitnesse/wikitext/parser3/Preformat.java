package fitnesse.wikitext.parser3;

class Preformat {

  static Symbol parse(Parser parser) {
    Token start = parser.advance();
    Symbol result = parser.textType(SymbolType.PREFORMAT).parseList(start);
    if (result.hasError()) return result;
    result.addFirst(Symbol.source(TokenType.PREFORMAT_START));
    result.add(Symbol.source(TokenType.PREFORMAT_END));
    return result;
  }
}
