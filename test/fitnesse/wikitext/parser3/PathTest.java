package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertScansWordAtStart;

public class PathTest {
  @Test public void scansPath() { assertScansWordAtStart("!path", "Path"); }

  @Test
  public void parses() {
    assertParses("PATH(SOURCE=!path ,TEXT=stuff)", "!path stuff\n");
    assertParses("PATH(SOURCE=!path ,TEXT=more,TEXT= ,TEXT=stuff)", "!path more stuff\n");
  }
}
