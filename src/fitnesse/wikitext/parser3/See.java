package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

public class See {
  public static String translate(Symbol symbol, Translator translator) {
    return new HtmlTag("b").body("See: " + translator.translate(symbol.getBranch(0))).htmlInline();
  }
}
