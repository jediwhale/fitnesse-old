package fitnesse.wikitext.parser3;

class Nesting {
  static Symbol parse(Parser parser) {
    parser.advance();
    Symbol result = new Symbol(SymbolType.NESTING);
    parser.parseToTerminator(result, NESTING_TERMINATOR);
    parser.advance();
    return result;
  }

  private static final Terminator NESTING_TERMINATOR = new Terminator(TokenType.NESTING_END);
}
