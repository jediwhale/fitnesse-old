package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.ParsingPage;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class IncludeTest {
  @Test public void scans() {
    assertScans("Include=!include,BlankSpace= ,Text=MyPage", "!include MyPage");
  }

  @Test public void parses() {
    FakeSourcePage source = new FakeSourcePage("root");
    ParsingPage page = new ParsingPage(source);
    FakeSourcePage.pages.clear();
    FakeSourcePage.addPage("root.MyPage", "stuff");
    assertParses("INCLUDE(WIKI_LINK=MyPage,LIST(TEXT=stuff))", "!include MyPage", page);
    assertParses("INCLUDE(WIKI_LINK=MyPage,LIST(TEXT=stuff))[-teardown=]", "!include -teardown MyPage", page);
  }

  @Test public void parsesNested() {
    FakeSourcePage source = new FakeSourcePage("root");
    ParsingPage page = new ParsingPage(source);
    FakeSourcePage.pages.clear();
    FakeSourcePage.addPage("root.MyPage", "!include AnOther");
    FakeSourcePage.addPage("root.MyPage.AnOther", "stuff");
    assertParses("INCLUDE(WIKI_LINK=MyPage,LIST(INCLUDE(WIKI_LINK=AnOther,LIST(TEXT=stuff))))", "!include MyPage", page);
  }

  @Test public void translates() {
    FakeSourcePage source = new FakeSourcePage("TopPage");
    ParsingPage page = new ParsingPage(source);
    FakeSourcePage.pages.clear();
    FakeSourcePage.addPage("TopPage.MyPage", "this is ${PAGE_NAME}");
    assertTranslates(collapsible("", "TopPage.MyPage"), "!include MyPage", page);
    assertTranslates(collapsible(" closed", "TopPage"), "!include -setup MyPage", page);
    assertTranslates("this is <a href=\"Fake.TopPage.MyPage\">TopPage.MyPage</a>", "!include -seamless MyPage", page);
  }

  private String collapsible(String closed, String pageName) {
    return "<div class=\"collapsible" + closed + "\"><ul><li><a href='#' class='expandall'>Expand</a></li><li><a href='#' class='collapseall'>Collapse</a></li></ul>\n" +
      "\t<p class=\"title\">Included page: <a href=\"Fake.MyPage\">MyPage</a></p>\n" +
      "\t<div>this is <a href=\"Fake." + pageName + "\">" + pageName + "</a></div>\n" +
      "</div>\n";
  }
}
