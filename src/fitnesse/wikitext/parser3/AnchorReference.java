package fitnesse.wikitext.parser3;

public class AnchorReference {
  public static String translate(Symbol symbol, Translator translator) {
    return Html.anchor("#" + symbol.getContent(), ".#" + symbol.getContent());
  }
}
