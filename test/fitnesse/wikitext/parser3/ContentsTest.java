package fitnesse.wikitext.parser3;

import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiSourcePage;
import fitnesse.wikitext.parser.TestRoot;
import fitnesse.wikitext.shared.Names;
import fitnesse.wikitext.shared.ParsingPage;
import org.junit.Before;
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
    ParsingPage page = makeParsingPage();
    page.putVariable(Names.REGRACE_TOC, "true");
    assertParses("CONTENTS[-R=2,REGRACE_TOC=true]", "!contents -R2", page);
  }

  @Test public void translates() {
    TestRoot root = new TestRoot();
    WikiPage pageOne = root.makePage("PageOne");
    root.makePage(pageOne, "PageTwo");
    root.makePage(pageOne, "PageThree");
    page = new ParsingPage(new WikiSourcePage(pageOne));
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
      "!contents", page);
  }

  @Before
  public void SetUp() {
    page = makeParsingPage();
  }

  private ParsingPage page;
}
