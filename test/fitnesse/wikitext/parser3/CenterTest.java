package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class CenterTest {
  @Test public void scans() {
    assertScansKeyword("!c", "Center");
  }

  @Test public void parses() {
    assertParses("CENTER(LIST(TEXT=stuff))", "!c stuff");
    assertParses("CENTER(LIST(TEXT=stuff))", "!c stuff\n");
  }

  @Test public void translates() {
    assertTranslates("<center>stuff</center>", "!c stuff");
  }
}
