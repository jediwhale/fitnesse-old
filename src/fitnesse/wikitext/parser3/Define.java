package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.shared.VariableStore;

import java.util.function.BiFunction;
import java.util.function.Function;

class Define {
  static Symbol parse(Parser parser, VariableStore variables) {
    Symbol result = new BranchSymbol(SymbolType.DEFINE);
    if (!parser.peek(2).isWord()) {
      return parser.makeError("Name must be alphanumeric", 2);
    }
    if (!parser.peek(3).isType(DelimiterType.BLANK_SPACE)) {
      return parser.makeError("Missing blank space", 3);
    }
    if (!parser.peek(4).isStartType()) {
      return parser.makeError("Expected { ( or [", 4);
    }
    parser.advance();
    parser.advance(); //todo: check blank
    result.add(parser.parseCurrent());
    parser.advance(); //todo: check blank

    //todo: change what's in define types

    Function<String, Symbol> mapText = text -> {
      result.add(Symbol.text(text));
      variables.putVariable(result.getContent(0), result.getContent(1));
      return result;
    };

    BiFunction<String, String, Symbol> mapError = (text, error) ->
      Symbol.makeList(mapText.apply(text), Symbol.error(error));

    return parser
      .withTokenTypes(TokenTypes.DEFINE_TYPES)
      .collectText(Terminator.make(parser.advance()), mapText, mapError);
  }

  static Symbol parseNested(Parser parser) { //todo: can be removed with changes to define-types?
    // this is used when a define appears inside another define
    // it does not return a parse tree or define a variable
    // it just advances through the tokens to accumulate text for the outer define
    parser.advance();
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
