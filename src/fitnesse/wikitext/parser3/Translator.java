package fitnesse.wikitext.parser3;

public interface Translator extends TranslateSymbol<String> {
  Translator copy();
  Translator substitute(SymbolType original, SymbolType substitute);
}
