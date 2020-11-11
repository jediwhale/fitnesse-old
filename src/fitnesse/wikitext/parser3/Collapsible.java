package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.Names;

class Collapsible {
  static Symbol parse(Parser parser) {
    Symbol result = new TaggedSymbol(SymbolType.COLLAPSIBLE);
    parser.advance();
    if (parser.peek(0).getContent().equals(">")) {
      result.putProperty(Names.STATE, Names.CLOSED);
      parser.advance();
    }
    else if (parser.peek(0).getContent().equals("<")) {
      result.putProperty(Names.STATE, Names.INVISIBLE);
      parser.advance();
    }
    parser.advance(); //todo: check blankspace
    result.add(parser.parseList(SymbolType.LIST, Terminator.END_LINE));
    result.add(parser.parseList(SymbolType.LIST, END_COLLAPSIBLE));
    parser.advance(); //todo: check newline
    return result;
  }

  static final Terminator END_COLLAPSIBLE = new Terminator(TokenType.COLLAPSIBLE_END);
}
