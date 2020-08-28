package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlElement;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class AnchorNameTest {

  @Test
  public void scans() {
    assertScans("AnchorName=!anchor ,Text=name", "!anchor name");
    assertScans("Text=!,BlankSpace= ,Text=anchor,BlankSpace= ,Text=name", "! anchor name");
    assertScans("Text=!anchorname", "!anchorname");
  }

  @Test
  public void parses() {
    assertParses("ANCHOR_NAME=name", "!anchor name");
  }

  @Test
  public void translates() {
    assertTranslates(html("name"), "!anchor name");
    assertTranslates("say" + html("no") + " more", "say!anchor no more");
    assertTranslates("say " + html("no") + " more", "say !anchor no more");
    assertTranslates(ERROR + "{name}", "!anchor {name}");
    assertTranslates(ERROR + "n@me", "!anchor n@me");
  }

  private static final String ERROR = toError("!anchor  Name must be alphanumeric");

  private String html(String name) {
    return "<a name=\"" + name + "\"/>" + HtmlElement.endl;
  }
}
