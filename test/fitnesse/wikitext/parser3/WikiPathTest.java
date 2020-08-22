package fitnesse.wikitext.parser3;

import org.junit.Assert;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class WikiPathTest {
  @Test
  public void checksWords() {
    assertWikiWord(true, "HiThere");
    assertWikiWord(true, "H1Th3re");
    assertWikiWord(true, "HiT");
    assertWikiWord(false, "hiThere");
    assertWikiWord(false, "Hithere");
    assertWikiWord(false, "HIThere");
    assertWikiWord(false, "HiTHere");
    assertWikiWord(false, "Hi&There");
  }

  @Test
  public void scansWikiWords() {
    assertScans("WikiPath=HiThere", "HiThere");
    assertScans("WikiPath=HiThere.BoB", "HiThere.BoB");
    assertScans("WikiPath=<HiThere.BoB", "<HiThere.BoB");
  }

  @Test
  public void parses() {
    assertParses("WIKI_LINK=HiThere", "HiThere");
    assertParses("WIKI_LINK=HiThere.BoB", "HiThere.BoB");
  }

  @Test
  public void translates() {
    assertTranslates(Html.anchor("Fake.HiThere", "HiThere"), "HiThere");
    assertTranslates("NewPage" + Html.anchor("Fake.NewPage?edit&amp;nonExistent=true", "title=\"create page\"", "[?]"), "NewPage");
  }

  private void assertWikiWord(boolean expected, String input) {
    Assert.assertEquals(input, expected, WikiPath.isWikiWord(new Text(input)));
  }
}
