package fitnesse.wikitext.parser3;

public class AnchorName {
  public static String translate(Symbol symbol, Translator translator) {
    return "<a name=\"" + symbol.getContent() + "\" />";
  }
}
