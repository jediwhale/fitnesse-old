package fitnesse.wikitext.parser3;

class Literal {

  static void scan(Content content, TokenList tokens) {
    new Scanner(TokenType.LITERAL_END).scan(content, tokens);
  }

  static Symbol parse(Parser parser) {
    parser.advance();
    Symbol result = new Parser(parser).parseList(parser.peek(-1));
    if (result.hasError()) return result;
    result.addFirst(Symbol.source(TokenType.LITERAL_START));
    result.add(Symbol.source(TokenType.LITERAL_END));
    return result;
  }
}
