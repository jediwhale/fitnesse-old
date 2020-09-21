package fitnesse.wikitext.parser3;

class Literal {

  static Symbol parse(Parser parser) {
    Token start = parser.advance();
    Symbol result = new Parser(parser).parseList(start);
    if (result.hasError()) return result;
    result.addFirst(Symbol.source(TokenType.LITERAL_START));
    result.add(Symbol.source(TokenType.LITERAL_END));
    return result;
  }
}
