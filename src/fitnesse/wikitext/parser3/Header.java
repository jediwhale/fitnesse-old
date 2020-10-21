package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.VariableStore;
import fitnesse.wikitext.parser.LineRule;

class Header {
  static Symbol parse(Parser parser, VariableStore variables) {
    Symbol result = new TaggedSymbol(SymbolType.HEADER);
    result.putProperty(LineRule.LEVEL, parser.advance().getContent().substring(1, 2));
    result.putProperty(LineRule.ID, Integer.toString(variables.nextId()));
    result.add(parser.parseList(SymbolType.LIST, Terminator.END_LINE));
    return result;
  }

  static String translate(Symbol symbol, Translator translator) {
    HtmlTag result = new HtmlTag("h" + symbol.findProperty(LineRule.LEVEL, "1"));
    result.add(symbol.translateChildren(translator));
    symbol.findProperty(LineRule.ID).ifPresent(id -> result.addAttribute("id", id));
    return result.html();
  }
}
