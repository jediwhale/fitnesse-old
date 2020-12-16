package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.shared.VariableStore;

class Define {
  static Symbol parse(Parser parser, VariableStore variables) {
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
    result.add(Symbol.text(parser.withTokenTypes(TokenTypes.DEFINE_TYPES).parseText(Terminator.make(parser.advance()))));

    variables.putVariable(result.getContent(0), result.getContent(1));
    return result;
  }

  static Symbol parseNested(Parser parser) {
    // this is used when a define appears inside another define
    // it does not return a parse tree or define a variable
    // it just advances through the tokens to accumulate text for the outer define
    parser.advance();
    parser.parseCurrent();
    parser.advance();
    parser.parseList(parser.advance());
    return Symbol.text("");
  }

  static String translate(Symbol symbol, Translator translator) {
    return HtmlTag.name("span")
      .attribute("class", "meta")
      .text("variable defined: " + symbol.getContent(0) + "=" + symbol.getContent(1))
      .htmlInline();
  }
}
