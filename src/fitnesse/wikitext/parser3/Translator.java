package fitnesse.wikitext.parser3;

public interface Translator extends TranslateSymbol {
  Translator copy();
  Translator substitute(SymbolType original, SymbolType substitute);
  External getExternal();
}
