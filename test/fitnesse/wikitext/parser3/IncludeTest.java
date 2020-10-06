package fitnesse.wikitext.parser3;

import fitnesse.wikitext.parser.Maybe;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class IncludeTest {
  @Test public void scans() {
    assertScans("Include=!include ,Text=MyPage", "!include MyPage");
  }

  @Test public void parses() {
    external.pageContent = new Maybe<>("stuff");
    assertParses("INCLUDE(TEXT=MyPage,LIST(TEXT=stuff))", "!include MyPage");
  }

  @Test public void translates() {
    external.pageContent = new Maybe<>("stuff");
    assertTranslates("<span>MyPage</span>\n<div class=\"collapsible closed\">stuff</div>\n", "!include MyPage");
    assertTranslates("<span>MyPage</span>\n<div class=\"collapsible closed\">stuff</div>\n", "!include -setup MyPage");
  }
}
