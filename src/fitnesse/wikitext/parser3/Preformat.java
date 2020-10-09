package fitnesse.wikitext.parser3;

class Preformat {

  static Symbol parse(Parser parser) {
    Token start = parser.advance();
    Symbol result = parser.textType(SymbolType.TEXT).parseList(start); //todo: or should be LITERAL??
    if (result.hasError()) return result;
    result.addFirst(Symbol.source(TokenType.PREFORMAT_START));
    result.add(Symbol.source(TokenType.PREFORMAT_END));
    return result;
  }
}
