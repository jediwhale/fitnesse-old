package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.parser.Maybe;

class Include {
  static Symbol parse(Parser parser, External external) {
    parser.advance();
    Token token;
    do {
      token = parser.advance();
    } while (!token.isType(TokenType.TEXT) || token.getContent().startsWith("-"));
    Maybe<String> includedContent = external.findPageContent(token.getContent());
    return includedContent.isNothing()
      ? new Symbol(SymbolType.ERROR, includedContent.because())
      : Symbol.make(SymbolType.INCLUDE,
          new Symbol(SymbolType.TEXT, token.getContent()),
          parser.parse(includedContent.getValue()));
  }

  static String translate(Symbol symbol, Translator translator) {
    return HtmlTag.name("span").body(symbol.getContent(0)).htmlInline() +
      translator.translate(symbol.getChild(1));
  }
}
