package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertScans;

public class IncludeTest {
  @Test public void scans() {
    assertScans("Include=!include ,Text=MyPage", "!include MyPage");
  }

  @Test public void parses() {
    assertParses("INCLUDE", "!include MyPage");
  }
}
