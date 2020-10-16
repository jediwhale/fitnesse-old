package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class ContentsTest {
  @Test public void scans() {
    assertScans("Contents=!contents", "!contents");
  }

  @Test public void parses() {
    assertParses("CONTENTS", "!contents");
  }

  @Test public void translates() {
    assertTranslates("<div class=\"contents\">\n" +
      "\t<b>Contents:</b>\n" +
      "</div>\n", "!contents");
  }
}
