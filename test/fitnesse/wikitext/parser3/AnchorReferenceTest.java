package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class AnchorReferenceTest {
  @Test
  public void scans() {
    assertScans("AnchorReference=.#,Text=anchorname", ".#anchorname");
    assertScans("AnchorReference=.#,BlankSpace= ,Text=anchorname", ".# anchorname");
    assertScans("Text=.,BlankSpace= ,Text=#anchorname", ". #anchorname");
  }

  @Test
  public void parses() {
    assertParses("ANCHOR_REFERENCE=anchorname", ".#anchorname");
  }

  @Test
  public void translates() {
    assertTranslates(Html.anchor("#anchorname", ".#anchorname"), ".#anchorname");
    assertTranslates("say" + Html.anchor("#anchorname", ".#anchorname") + " now", "say.#anchorname now");
    assertTranslates(toError(".# Name must be alphanumeric") + " anchorname", ".# anchorname");
    assertTranslates(toError(".# Name must be alphanumeric") + "n@me", ".#n@me");
  }
}
