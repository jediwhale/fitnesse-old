package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.ParsingPage;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertTranslates;

public class LastModifiedTest {
  @Test public void translates() {
    FakeSourcePage source = new FakeSourcePage();
    ParsingPage page = new ParsingPage(source);
    source.properties.put("LastModifyingUser", "Joe");
    source.properties.put("LastModified", "yesterday");
    assertTranslates("<span class=\"meta\">Last modified by Joe on yesterday</span>", "!lastmodified", page);
  }
}
