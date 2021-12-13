package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.Names;

public class Style {
  public static Symbol parse(Parser parser) {
    String contents = parser.peek(0).getContent() + parser.peek(1).getContent();
    String cssClass = contents.substring(7);
    if (!parser.peek(1).isType(TokenType.TEXT) || cssClass.length() == 0) {
      return parser.makeError("Expected class name", 2);
    }
    if (!parser.peek(2).isStartType()) {
      return parser.makeError("Expected { ( or [", 2);
    }
    Symbol result = new BranchSymbol(SymbolType.STYLE);
    result.putProperty(Names.CLASS, cssClass);
    parser.advance();
    parser.advance();
    Token start = parser.peek(0);
    parser.advance();
    Symbol list = parser.parseList(SymbolType.LIST, Terminator.make(start, contents));
    if (list.hasError()) return list;
    result.add(list);
    return result;
  }
}
