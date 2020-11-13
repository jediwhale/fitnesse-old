package fitnesse.wikitext.parser3;

import fitnesse.wikitext.VariableStore;
import fitnesse.wikitext.parser.Collapsible;
import fitnesse.wikitext.parser.Maybe;
import fitnesse.wikitext.shared.Names;

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
          result.hasProperty(Names.SETUP) || result.hasProperty(Names.TEARDOWN)
            ? parser.withContent(included.getValue().getSourcePage().getContent()).parseToEnd() //todo: not sure this is correct, maybe bug in v2
            : Parser.parse(included.getValue().getSourcePage().getContent(), ParseRules.make(variables, included.getValue())));

    if (result.hasProperty(Names.SETUP)) variables.findVariable(Names.COLLAPSE_SETUP).ifPresent(value -> result.putProperty(Names.COLLAPSE_SETUP, value));
    if (result.hasProperty(Names.TEARDOWN)) variables.findVariable(Names.COLLAPSE_TEARDOWN).ifPresent(value -> result.putProperty(Names.COLLAPSE_TEARDOWN, value));
    return result;
  }

  static String translate(Symbol symbol, Translator translator) {
    if (symbol.hasProperty(Names.SEAMLESS)) {
      return translator.translate(symbol.getBranch(1));
    }
    String closeState = "";
    if ((symbol.hasProperty(Names.SETUP) && symbol.findProperty(Names.COLLAPSE_SETUP).orElse("true").equals("true"))
//      || (symbol.hasTag("-teardown") && symbol.findTag(COLLAPSE_TEARDOWN).orElse("true").equals("true"))
      || symbol.hasProperty(Names.TEARDOWN) //todo: to make test pass, but seems like v2 bug
      || symbol.hasProperty(Names.COLLAPSE)) {
      closeState = Collapsible.CLOSED;
    }
    Collection<String> extraCollapsibleClass =
      symbol.hasProperty(Names.TEARDOWN) ? Collections.singleton(Names.TEARDOWN_CLASS) : Collections.emptySet();//todo: but not for setup??
    String title = "Included page: " + translator.translate(symbol.getBranch(0));
    return Collapsible.generateHtml(closeState, title, translator.translate(symbol.getBranch(1)), extraCollapsibleClass);
  }

}
