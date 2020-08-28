package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

public class Html {

  public static String anchor(String reference, String content) {
    return HtmlTag.name("a").attribute("href", reference).body(content).htmlInline();
  }

}
