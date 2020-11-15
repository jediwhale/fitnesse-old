package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertTranslates;

public class LastModifiedTest {
  @Test public void translates() {
    FakeSourcePage page = new FakeSourcePage();
    FakeExternal external = new FakeExternal(page);
    page.properties.put("LastModifyingUser", "Joe");
    page.properties.put("LastModified", "yesterday");
    assertTranslates("<span class=\"meta\">Last modified by Joe on yesterday</span>", "!lastmodified", external);
  }
}
