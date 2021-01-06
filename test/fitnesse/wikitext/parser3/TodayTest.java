package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertScans;

public class TodayTest {
  @Test public void scans() {
    assertScans("Today=!today", "!today");
    assertScans("Text=!todays", "!todays");
  }

  @Test public void parses() {
    assertParses("TODAY", "!today");
    assertParses("TODAY[Increment=+1]", "!today +1");
  }
}
