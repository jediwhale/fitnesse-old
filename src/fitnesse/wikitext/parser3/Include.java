package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.VariableStore;
import fitnesse.wikitext.parser.Maybe;

import java.util.Optional;

class Include {
  static Symbol parse(Parser parser, External external, VariableStore variables) {
    parser.advance();
    String option = "";
    if (parser.peek(0).isType(TokenType.TEXT) && parser.peek(0).getContent().startsWith("-")) {
      option = parser.advance().getContent();
      parser.advance(); //todo: check blank
    }

    Token pagePath= parser.advance();;
    Maybe<External> included = external.make(pagePath.getContent());
    return included.isNothing()
      ? new SymbolLeaf(SymbolType.ERROR, included.because())
      : Symbol.make(SymbolType.INCLUDE,
          new SymbolLeaf(SymbolType.TEXT, pagePath.getContent()),
          isSetupTeardown(option)
            ? parser.withContent(included.getValue().pageContent()).parseToEnd() //todo: not sure this is correct, maybe bug in v2
            : Parser.parse(included.getValue().pageContent(), ParseRules.make(variables, included.getValue())));
  }

  private static boolean isSetupTeardown(String option) {
    return option.equals("-setup") || option.equals("-teardown");
  }

  static String translate(Symbol symbol, Translator translator) {
    return
      HtmlTag.name("span").body(symbol.getContent(0)).html() +
      HtmlTag.name("div").attribute("class", "collapsible closed")
        .body(HtmlTag.name("div").body(translator.translate(symbol.getChild(1))).html())
        .html();
  }
}
