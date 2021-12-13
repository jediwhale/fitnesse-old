package fitnesse.wikitext.parser3;

import fitnesse.wikitext.parser.Collapsible;
import fitnesse.wikitext.parser.Maybe;
import fitnesse.wikitext.shared.Names;
import fitnesse.wikitext.shared.ParsingPage;
import fitnesse.wikitext.shared.SourcePage;
import fitnesse.wikitext.shared.VariableStore;

import java.util.Collection;
import java.util.Collections;

class Include {

  static Symbol parse(Parser parser, ParsingPage page, VariableStore variables) {
    parser.advance();
    parser.advance(); //todo: check blank
    Symbol result = new BranchSymbol(SymbolType.INCLUDE);
    if (parser.peek(0).isType(TokenType.TEXT) && parser.peek(0).getContent().startsWith("-")) {
      result.putProperty(parser.advance().getContent(), "");
      parser.advance(); //todo: check blank
    }

    Symbol pagePath = parser.parseCurrent();
    Maybe<ParsingPage> included = makeIncluded(page, pagePath.getContent());
    if (included.isNothing()) {
      return new LeafSymbol(SymbolType.ERROR, included.because());
    }
    result.add(pagePath);
    result.add(
          result.hasProperty(Names.SETUP) || result.hasProperty(Names.TEARDOWN)
            ? parser.withContent(included.getValue().getNamedPage().getContent()).parseToEnd()
            : Parser.parse(
                included.getValue().getNamedPage().getContent(),
                included.getValue()));

    if (result.hasProperty(Names.SETUP)) variables.findVariable(Names.COLLAPSE_SETUP).ifPresent(value -> result.putProperty(Names.COLLAPSE_SETUP, value));
    if (result.hasProperty(Names.TEARDOWN)) variables.findVariable(Names.COLLAPSE_TEARDOWN).ifPresent(value -> result.putProperty(Names.COLLAPSE_TEARDOWN, value));
    return result;
  }

  private static Maybe<ParsingPage> makeIncluded(ParsingPage page, String pageName) {
    Maybe<SourcePage> includedPage = page.getNamedPage().findIncludedPage(pageName);
    if (includedPage.isNothing()) return Maybe.nothingBecause(includedPage.because());
    return new Maybe<>(page.copyForNamedPage(includedPage.getValue()));
  }

  static String translate(Symbol symbol, Translator translator) {
    if (symbol.hasProperty(Names.SEAMLESS)) {
      return translator.translate(symbol.getBranch(1));
    }
    String closeState = "";
    if ((symbol.hasProperty(Names.SETUP) && symbol.findProperty(Names.COLLAPSE_SETUP).orElse("true").equals("true"))
//todo:     || (symbol.hasTag("-teardown") && symbol.findTag(COLLAPSE_TEARDOWN).orElse("true").equals("true"))
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
