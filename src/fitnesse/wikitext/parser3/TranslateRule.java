package fitnesse.wikitext.parser3;

@FunctionalInterface
public interface TranslateRule {
  String translate(Symbol symbol, Translator translator);
}
