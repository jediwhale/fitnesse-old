package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

public class AnchorReference {
  public static String translate(Symbol symbol, Translator translator) {
    String content = symbol.translateContent(translator);
    return HtmlTag.name("a").attribute("href", "#" + content).body(".#" + content).htmlInline();
  }
}
