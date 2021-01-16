package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.Names;

class Today {
  static Symbol parse(Parser parser) {
    Symbol result = new TaggedSymbol(SymbolType.TODAY);
    parser.advance();
    while (parser.peek(0).isType(DelimiterType.BLANK_SPACE) &&
      (parser.peek(1).getContent().startsWith("-") || parser.peek(1).getContent().startsWith("+"))) {
      parser.advance();
      Token option = parser.advance();
      result.putProperty(Names.INCREMENT, option.getContent());
    }
    return result;
  }
}
