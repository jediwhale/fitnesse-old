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
    assertParses("INCLUDE(WIKI_LINK=MyPage,LIST(TEXT=stuff))", "!include MyPage");
  }

  @Test public void parsesNested() {
    FakeExternal.pages.clear();
    FakeExternal.pages.put("root.MyPage", "!include AnOther");
    FakeExternal.pages.put("root.MyPage.AnOther", "stuff");
    assertParses("INCLUDE(WIKI_LINK=MyPage,LIST(INCLUDE(WIKI_LINK=AnOther,LIST(TEXT=stuff))))", "!include MyPage");
  }

  @Test public void translates() {
    FakeExternal external = new FakeExternal(new FakeSourcePage());
    external.putVariable("PAGE_NAME", "TopPage");
    FakeExternal.pages.clear();
    FakeExternal.pages.put("root.MyPage", "this is ${PAGE_NAME}");
    assertTranslates(collapsible("", "MyPage"), "!include MyPage", external);
    assertTranslates(collapsible(" closed", "TopPage"), "!include -setup MyPage", external);
    assertTranslates("this is <a href=\"Fake.MyPage\">MyPage</a>", "!include -seamless MyPage", external);
  }

  private String collapsible(String closed, String pageName) {
    return "<div class=\"collapsible" + closed + "\"><ul><li><a href='#' class='expandall'>Expand</a></li><li><a href='#' class='collapseall'>Collapse</a></li></ul>\n" +
      "\t<p class=\"title\">Included page: <a href=\"Fake.MyPage\">MyPage</a></p>\n" +
      "\t<div>this is <a href=\"Fake." + pageName + "\">" + pageName + "</a></div>\n" +
      "</div>\n";
  }
}
