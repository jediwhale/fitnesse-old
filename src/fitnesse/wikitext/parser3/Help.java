package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.Names;

class Help {
  static Symbol parse(Parser parser) {
    Symbol result =  new TaggedSymbol(SymbolType.HELP);
    parser.advance();
    if (parser.peek(0).isType(TokenType.BLANK_SPACE) && parser.peek(1).getContent().equals(Names.EDITABLE)) {
      result.putProperty(Names.EDITABLE, "");
      parser.advance();
      parser.advance();
    }
    return result;
  }
}
