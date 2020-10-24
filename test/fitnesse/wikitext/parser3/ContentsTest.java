package fitnesse.wikitext.parser3;

import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiSourcePage;
import fitnesse.wikitext.parser.TestRoot;
import fitnesse.wikitext.shared.Names;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class ContentsTest {
  @Test public void scans() {
    assertScans("Contents=!contents", "!contents");
  }

  @Test public void parses() {
    assertParses("CONTENTS", "!contents");
  }

  @Test public void parsesWithProperties() {
    assertParses("CONTENTS[-R=2147483647,-f=,-g=]", "!contents -f -g -R");
    external.putVariable(Names.REGRACE_TOC, "true");
    assertParses("CONTENTS[REGRACE_TOC=true,-R=2]", "!contents -R2");
  }

  @Test public void translates() {
    TestRoot root = new TestRoot();
    WikiPage pageOne = root.makePage("PageOne");
    root.makePage(pageOne, "PageTwo");
    root.makePage(pageOne, "PageThree");
    external.sourcePage = new WikiSourcePage(pageOne);
    assertTranslates(
      "<div class=\"contents\">\n" +
      "\t<b>Contents:</b>\n" +
      "\t<ul class=\"toc1\">\n" +
      "\t\t<li>\n" +
      "\t\t\t<a href=\"PageOne.PageThree\" class=\"static\">PageThree</a>\n" +
      "\t\t</li>\n" +
      "\t\t<li>\n" +
      "\t\t\t<a href=\"PageOne.PageTwo\" class=\"static\">PageTwo</a>\n" +
      "\t\t</li>\n" +
      "\t</ul>\n" +
      "</div>\n",
      "!contents");
  }
}
