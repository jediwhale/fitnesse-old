package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

public class Variable {
  public static Symbol parsePut(Parser parser) {
    Symbol result = new Symbol(SymbolType.DEFINE);
    if (!parser.peek(1).isVariable()) {
      return parser.makeError("Name must be alphanumeric", 1);
    }
    if (!parser.peek(2).isType(TokenType.BLANK_SPACE)) {
      return parser.makeError("Missing blank space", 2);
    }
    if (!parser.peek(3).isStartType()) {
      return parser.makeError("Expected { ( or [", 3);
    }
    parser.advance();
    result.add(parser.parseCurrent());
    parser.advance();
    parser.advance();
    result.add(new Symbol(SymbolType.TEXT, parser.parseText(parser.peek(-1))));

    parser.putVariable(result.getContent(0), result.getContent(1));
    return result;
  }

  public static Symbol parseGet(Parser parser) {
    parser.advance();
    String name = parser.peek(0).getContent();
    Symbol result = parser.getVariable(name)
      .map(parser::parseString)
      .orElseGet(() -> Symbol.error("Undefined variable: " + name));
    parser.advance();
    parser.advance(); //check braceend
    return result;
  }

  public static String translate(Symbol symbol, Translator translator) {
    return HtmlTag.name("span")
      .attribute("class", "meta")
      .text("variable defined: " + symbol.getContent(0) + "=" + symbol.getContent(1))
      .htmlInline();
  }
}
