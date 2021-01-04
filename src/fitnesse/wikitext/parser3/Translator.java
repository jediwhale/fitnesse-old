package fitnesse.wikitext.parser3;

public interface Translator {
  TranslateRule findRule(SymbolType symbolType);

  default String translate(Symbol symbol) {
    return findRule(symbol.getType()).translate(symbol, this);
  }
}
