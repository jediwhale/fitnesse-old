package fitnesse.wikitext.parser3;

public interface Translator {
  String translate(Symbol symbol);
  Translator copy();
  Translator substitute(SymbolType original, SymbolType substitute);
  External getExternal();
}
