package fitnesse.wikitext.parser3;

import org.junit.Assert;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class PathTest {
  @Test public void scansPath() { assertScansWordAtStart("!path", "Path"); }

  @Test public void parses() {
    assertParses("PATH(SOURCE=!path ,TEXT=stuff)", "!path stuff\n");
    assertParses("PATH(SOURCE=!path ,TEXT=more,TEXT= ,TEXT=stuff)", "!path more stuff\n");
  }

  @Test public void translates() {
    assertTranslates("<span class=\"meta\">classpath: stuff</span>", "!path stuff");
  }

  @Test public void providesPaths() {
    StringBuilder paths = new StringBuilder();
    Path.providePaths(parse("!path stuff").getChild(0), paths::append);
    Assert.assertEquals("stuff", paths.toString());
  }
}
