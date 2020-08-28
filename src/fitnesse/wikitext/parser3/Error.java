package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

public class Error {
  public static String translate(Symbol symbol, Translator translator) {
    return " " + HtmlTag.name("span").attribute("class", "error").body(symbol.translateContent(translator)).htmlInline() + " ";
  }
}
