package fitnesse.wikitext.parser3;

class Include {
  static Symbol parse(Parser parser, External external) {
    parser.advance();
    Token token;
    while (true) {
      token = parser.advance();
      if (token.isType(TokenType.TEXT) && !token.getContent().startsWith("-")) break;
    }
    String includedContent = external.findPageContent(token.getContent()).getValue();
    return Symbol.make(SymbolType.INCLUDE, parser.parse(includedContent));
  }

  static String translate(Symbol symbol, Translator translator) {
    return "x";
  }
}
