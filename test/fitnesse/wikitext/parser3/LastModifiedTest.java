package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertTranslates;
import static fitnesse.wikitext.parser3.Helper.external;

public class LastModifiedTest {
  @Test public void translates() {
    external.sourcePage = new FakeSourcePage();
    assertTranslates("<span class=\"meta\">Last modified by LastModifyingUserValue on LastModifiedValue</span>", "!lastmodified");
  }
}
