package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.ParsingPage;
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
    FakeSourcePage source = new FakeSourcePage();
    ParsingPage page = new ParsingPage(source);
    source.properties.put("Help", "");
    assertTranslates("", "!help", page);
    assertTranslates(" <a href=\"FullPath?properties\">(edit help text)</a>", "!help -editable", page);
    source.properties.put("Help", "some help text");
    assertTranslates("some help text", "!help", page);
    assertTranslates("some help text <a href=\"FullPath?properties\">(edit)</a>", "!help -editable", page);
  }
}
