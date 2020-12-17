package fitnesse.wikitext.parser3;

public interface Translator {
  TranslateRule findRule(SymbolType symbolType);
  void decorate(Symbol symbol);

  default String translate(Symbol symbol) {
    decorate(symbol);
    return findRule(symbol.getType()).translate(symbol, this);
  }
}
