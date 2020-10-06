package fitnesse.wikitext.parser3;

import fitnesse.wikitext.parser.FormattedExpression;
import fitnesse.wikitext.parser.Maybe;

class Expression {
  static Symbol parse(Parser parser) {
    String expression = parser.parseText(parser.advance());
    Maybe<String> result = new FormattedExpression(expression, Maybe.noString).evaluate();
    if (result.isNothing()) return Symbol.error(result.because());
    parser.putInput(result.getValue());
    return new Symbol(SymbolType.TEXT, "");
  }
}
