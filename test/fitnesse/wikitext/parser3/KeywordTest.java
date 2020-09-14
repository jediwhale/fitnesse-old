package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertScans;

public class KeywordTest {
  @Test
  public void scansNote() {
    assertScans("Note=!note ,Text=something", "!note something");
  }

  @Test
  public void scansPath() {
    assertScans("Path=!path ,Text=something", "!path something");
  }

  @Test
  public void parsesPath() {
    assertParses("PATH(TEXT=stuff)", "!path stuff");
  }

  @Test
  public void scansSee() {
    assertScans("See=!see ,Text=something", "!see something");
  }

  @Test
  public void parsesSee() {
    assertParses("SEE(TEXT=stuff)", "!see stuff");
  }
}
