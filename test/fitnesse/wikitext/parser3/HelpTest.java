package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class HelpTest {

  @Test public void scans() {
    assertScans("Help=!help", "!help");
  }

  @Test public void parses() {
    assertParses("HELP", "!help");
    assertParses("HELP[-editable=]", "!help -editable");
  }

  @Test public void translates() {
    FakeSourcePage page = new FakeSourcePage();
    FakeExternal external = new FakeExternal(page);
    page.properties.put("Help", "");
    assertTranslates("", "!help", external);
    assertTranslates(" <a href=\"FullPath?properties\">(edit help text)</a>", "!help -editable", external);
    page.properties.put("Help", "some help text");
    assertTranslates("some help text", "!help", external);
    assertTranslates("some help text <a href=\"FullPath?properties\">(edit)</a>", "!help -editable", external);
  }
}
