package fitnesse.wikitext.parser.decorator;

import fitnesse.wikitext.parser.Symbol;
import fitnesse.wikitext.VariableSource;

// @deprecated use SyntaxNodeDecorator
@Deprecated
public interface ParsedSymbolDecorator {
  void handleParsedSymbol(Symbol symbol, VariableSource variableSource);
}
