package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.ParsingPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class PathTest {
  @Test public void scans() {
    assertScansKeyword("!path", "Path");
  }

  @Test public void parses() {
    assertParses("PATH(TEXT=stuff)", "!path stuff\n");
    assertParses("PATH(TEXT=more,TEXT= ,TEXT=stuff)", "!path more stuff\n");
  }

  @Test public void translates() {
    assertTranslates("<span class=\"meta\">classpath: stuff</span>", "!path stuff");
  }

  @Test public void translatesWithVariable() {
    page.putVariable("x", "more");
    assertTranslates("<span class=\"meta\">classpath: morestuff</span>", "!path ${x}stuff\n", page);
  }

  @Test public void providesPaths() {
    StringBuilder paths = buildPath("!path stuff");
    Assert.assertEquals("stuff", paths.toString());
  }

  @Test public void providesPathsWithVariable() {
    page.putVariable("x", "more");
    StringBuilder paths = buildPath("!path ${x}stuff");
    Assert.assertEquals("morestuff", paths.toString());
  }

  private StringBuilder buildPath(String input) {
    StringBuilder paths = new StringBuilder();
    Path.providePaths(parse(input, page), paths::append);
    return paths;
  }

  @Before
  public void SetUp() {
    page = Helper.makeParsingPage();
  }

  private ParsingPage page;
}

