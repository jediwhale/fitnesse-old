package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.Names;
import fitnesse.wikitext.shared.VariableStore;

class Header {
  static Symbol parse(Parser parser, VariableStore variables) {
    Symbol result = new TaggedSymbol(SymbolType.HEADER);
    result.putProperty(Names.LEVEL, parser.advance().getContent().substring(1, 2));
    parser.advance(); //todo: check blank
    result.putProperty(Names.ID, Integer.toString(variables.nextId()));
    result.add(parser.parseList(SymbolType.LIST, Terminator.END_LINE));
    return result;
  }
}
