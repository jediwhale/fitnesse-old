package fitnesse.wikitext.parser3;

import fitnesse.wikitext.VariableStore;
import fitnesse.wikitext.parser.Collapsible;
import fitnesse.wikitext.parser.Maybe;

import java.util.Collection;
import java.util.Collections;

class Include {

  static Symbol parse(Parser parser, External external, VariableStore variables) {
    parser.advance();
    Symbol result = new TaggedSymbol(SymbolType.INCLUDE);
    if (parser.peek(0).isType(TokenType.TEXT) && parser.peek(0).getContent().startsWith("-")) {
      result.putProperty(parser.advance().getContent(), "");
      parser.advance(); //todo: check blank
    }

    Symbol pagePath = parser.parseCurrent();
    Maybe<External> included = external.make(pagePath.getContent());
    if (included.isNothing()) {
      return new LeafSymbol(SymbolType.ERROR, included.because());
    }
    result.add(pagePath);
    result.add(
          result.hasProperty("-setup") || result.hasProperty("-teardown")
            ? parser.withContent(included.getValue().getSourcePage().getContent()).parseToEnd() //todo: not sure this is correct, maybe bug in v2
            : Parser.parse(included.getValue().getSourcePage().getContent(), ParseRules.make(variables, included.getValue())));

    if (result.hasProperty("-setup")) variables.findVariable(COLLAPSE_SETUP).ifPresent(value -> result.putProperty(COLLAPSE_SETUP, value));
    if (result.hasProperty("-teardown")) variables.findVariable(COLLAPSE_TEARDOWN).ifPresent(value -> result.putProperty(COLLAPSE_TEARDOWN, value));
    return result;
  }

  static String translate(Symbol symbol, Translator translator) {
    String closeState = "";
    if ((symbol.hasProperty("-setup") && symbol.findProperty(COLLAPSE_SETUP).orElse("true").equals("true"))
//      || (symbol.hasTag("-teardown") && symbol.findTag(COLLAPSE_TEARDOWN).orElse("true").equals("true"))
      || symbol.hasProperty("-teardown") //todo: to make test pass, but seems like v2 bug
      || symbol.hasProperty("-c")) {
      closeState = Collapsible.CLOSED;
    }
    Collection<String> extraCollapsibleClass =
      symbol.hasProperty("-teardown") ? Collections.singleton("teardown") : Collections.emptySet();//todo: but not for setup??
    String title = "Included page: " + translator.translate(symbol.getBranch(0));
    return Collapsible.generateHtml(closeState, title, translator.translate(symbol.getBranch(1)), extraCollapsibleClass);
  }

  private static final String COLLAPSE_SETUP = "COLLAPSE_SETUP";
  private static final String COLLAPSE_TEARDOWN = "COLLAPSE_TEARDOWN";
}
