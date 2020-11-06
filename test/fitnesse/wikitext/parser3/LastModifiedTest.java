package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertTranslates;

public class LastModifiedTest {
  @Test public void translates() {
    assertTranslates("<span class=\"meta\">Last modified by LastModifyingUserValue on LastModifiedValue</span>", "!lastmodified");
  }
}
