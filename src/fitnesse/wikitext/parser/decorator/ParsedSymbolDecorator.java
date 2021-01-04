package fitnesse.wikitext.parser.decorator;

import fitnesse.wikitext.parser.Symbol;
import fitnesse.wikitext.shared.VariableSource;

public interface ParsedSymbolDecorator {
  void handleParsedSymbol(Symbol symbol, VariableSource variableSource);
}
