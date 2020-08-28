package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

public class AnchorName {
  public static String translate(Symbol symbol, Translator translator) {
    return HtmlTag.name("a").attribute("name", symbol.translateContent(translator)).html();
  }
}
