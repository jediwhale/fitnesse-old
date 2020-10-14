package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class IncludeTest {
  @Test public void scans() {
    assertScans("Include=!include ,Text=MyPage", "!include MyPage");
  }

  @Test public void parses() {
    FakeExternal.pages.clear();
    FakeExternal.pages.put("root.MyPage", "stuff");
    assertParses("INCLUDE(TEXT=MyPage,LIST(TEXT=stuff))", "!include MyPage");
  }

  @Test public void parsesNested() {
    FakeExternal.pages.clear();
    FakeExternal.pages.put("root.MyPage", "!include AnOther");
    FakeExternal.pages.put("root.MyPage.AnOther", "stuff");
    assertParses("INCLUDE(TEXT=MyPage,LIST(INCLUDE(TEXT=AnOther,LIST(TEXT=stuff))))", "!include MyPage");
  }

  @Test public void translates() {
    FakeExternal.pages.clear();
    FakeExternal.pages.put("root.MyPage", "stuff");
    assertTranslates("<span>MyPage</span>\n<div class=\"collapsible\"><div>stuff</div>\n</div>\n", "!include MyPage");
    assertTranslates("<span>MyPage</span>\n<div class=\"collapsible closed\"><div>stuff</div>\n</div>\n", "!include -setup MyPage");
  }
}
