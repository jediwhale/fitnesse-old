package fitnesse.wikitext.parser3;

import org.junit.Assert;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class PathTest {
  @Test public void scansPath() {
    assertScansWordAtStart("!path", "Path");
  }

  @Test public void parses() {
    assertParses("PATH(SOURCE=!path ,TEXT=stuff)", "!path stuff\n");
    assertParses("PATH(SOURCE=!path ,TEXT=more,TEXT= ,TEXT=stuff)", "!path more stuff\n");
  }

  @Test public void translates() {
    assertTranslates("<span class=\"meta\">classpath: stuff</span>", "!path stuff");
  }

  @Test public void translatesWithVariable() {
    external.putVariable("x", "more");
    assertTranslates("<span class=\"meta\">classpath: morestuff</span>", "!path ${x}stuff\n");
  }

  @Test public void providesPaths() {
    StringBuilder paths = buildPath("!path stuff");
    Assert.assertEquals("stuff", paths.toString());
  }

  @Test public void providesPathsWithVariable() {
    external.putVariable("x", "more");
    StringBuilder paths = buildPath("!path ${x}stuff");
    Assert.assertEquals("morestuff", paths.toString());
  }

  private StringBuilder buildPath(String input) {
    StringBuilder paths = new StringBuilder();
    Path.providePaths(parse(input), paths::append);
    return paths;
  }
}

