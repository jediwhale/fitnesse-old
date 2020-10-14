package fitnesse.wikitext.parser3;

import fitnesse.wikitext.parser.FormattedExpression;
import fitnesse.wikitext.parser.Maybe;

class Expression {
  static Symbol parse(Parser parser) {
    String expression = parser.parseText(Terminator.make(parser.advance()));
    Maybe<String> result = new FormattedExpression(expression, Maybe.noString).evaluate();
    if (result.isNothing()) return Symbol.error(result.because());
    return new LeafSymbol(SymbolType.TEXT, result.getValue());
  }
}
