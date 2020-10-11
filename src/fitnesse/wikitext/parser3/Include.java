package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.VariableStore;
import fitnesse.wikitext.parser.Maybe;

class Include {
  static Symbol parse(Parser parser, External external, VariableStore variables) {
    parser.advance();
    Token token;
    do {
      token = parser.advance();
    } while (!token.isType(TokenType.TEXT) || token.getContent().startsWith("-"));
    Maybe<External> included = external.make(token.getContent());
    return included.isNothing()
      ? new Symbol(SymbolType.ERROR, included.because())
      : Symbol.make(SymbolType.INCLUDE,
          new Symbol(SymbolType.TEXT, token.getContent()),
          Parser.parse(included.getValue().pageContent(), ParseRules.make(variables, included.getValue())));
  }

  static String translate(Symbol symbol, Translator translator) {
    return
      HtmlTag.name("span").body(symbol.getContent(0)).html() +
      HtmlTag.name("div").attribute("class", "collapsible closed")
        .body(translator.translate(symbol.getChild(1))).html();
  }
}
