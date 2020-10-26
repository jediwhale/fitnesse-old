package fitnesse.wikitext.parser3;

import fitnesse.wikitext.VariableStore;
import fitnesse.wikitext.shared.Names;

class Expression {
  static Symbol parse(Parser parser, VariableStore variables) {
    Symbol result = new TaggedSymbol(SymbolType.EXPRESSION);
    result.add(parser.parseList(parser.advance()));
    result.copyVariables(new String[] {Names.FORMAT_LOCALE}, variables);
    return result;
  }
}
