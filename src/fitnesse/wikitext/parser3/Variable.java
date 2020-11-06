package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.VariableSource;
import fitnesse.wikitext.VariableStore;

import java.util.Optional;

public class Variable {
  public static Symbol parsePut(Parser parser, VariableStore variables) {
    Symbol result = new BranchSymbol(SymbolType.DEFINE);
    if (!parser.peek(1).isWord()) {
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
    result.add(Symbol.text(parser.parseText(Terminator.make(parser.advance()))));

    variables.putVariable(result.getContent(0), result.getContent(1));
    return result;
  }

  public static Symbol parseGet(Parser parser, VariableSource variables) {
    parser.advance();
    String name = parser.advance().getContent(); //check text
    parser.advance(); //check braceend
    Optional<String> value = variables.findVariable(name);
    if (!value.isPresent()) return Symbol.error("Undefined variable: " + name);
    return parser.withContent(value.get()).parseToEnd();
  }

  public static String translate(Symbol symbol, Translator translator) {
    return HtmlTag.name("span")
      .attribute("class", "meta")
      .text("variable defined: " + symbol.getContent(0) + "=" + symbol.getContent(1))
      .htmlInline();
  }
}
