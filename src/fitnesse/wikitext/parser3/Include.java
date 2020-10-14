package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.VariableStore;
import fitnesse.wikitext.parser.Maybe;

class Include {

  static Symbol parse(Parser parser, External external, VariableStore variables) {
    parser.advance();
    Symbol result = new TaggedSymbol(SymbolType.INCLUDE);
    if (parser.peek(0).isType(TokenType.TEXT) && parser.peek(0).getContent().startsWith("-")) {
      result.putTag(parser.advance().getContent(), "");
      parser.advance(); //todo: check blank
    }

    Token pagePath= parser.advance();;
    Maybe<External> included = external.make(pagePath.getContent());
    if (included.isNothing()) {
      return new LeafSymbol(SymbolType.ERROR, included.because());
    }
    result.add(new LeafSymbol(SymbolType.TEXT, pagePath.getContent()));
    result.add(
          result.hasTag("-setup") || result.hasTag("-teardown")
            ? parser.withContent(included.getValue().pageContent()).parseToEnd() //todo: not sure this is correct, maybe bug in v2
            : Parser.parse(included.getValue().pageContent(), ParseRules.make(variables, included.getValue())));

    variables.findVariable(COLLAPSE_SETUP).ifPresent(value -> result.putTag(COLLAPSE_SETUP, value));
    variables.findVariable(COLLAPSE_TEARDOWN).ifPresent(value -> result.putTag(COLLAPSE_TEARDOWN, value));
    return result;
  }

  static String translate(Symbol symbol, Translator translator) {
    String cssClass = "collapsible";
    if ((symbol.hasTag("-setup") && symbol.findTag(COLLAPSE_SETUP).orElse("true").equals("true"))
//      || (symbol.hasTag("-teardown") && symbol.findTag(COLLAPSE_TEARDOWN).orElse("true").equals("true"))
      || (symbol.hasTag("-teardown") && symbol.findTag(COLLAPSE_SETUP).orElse("true").equals("true")) //todo: to make test pass, but seems like v2 bug
      || symbol.hasTag("-c")) {
      cssClass += " closed";
    }
    if (symbol.hasTag("-teardown")) cssClass += " teardown"; //todo: but not for setup??
    return
      HtmlTag.name("span").body("Included page: " + symbol.getContent(0)).html() +
      HtmlTag.name("div").attribute("class", cssClass)
        .body(HtmlTag.name("div").body(translator.translate(symbol.getChild(1))).html())
        .html();
  }

  private static final String COLLAPSE_SETUP = "COLLAPSE_SETUP";
  private static final String COLLAPSE_TEARDOWN = "COLLAPSE_TEARDOWN";
}
