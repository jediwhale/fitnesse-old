package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class CollapsibleTest {
  @Test public void scans() {
    assertScans("CollapsibleStart=!*,Text=stuff,CollapsibleEnd=*!", "!*stuff*!");
    assertScans("CollapsibleStart=!**,Text=stuff,CollapsibleEnd=**!", "!**stuff**!");
  }

  @Test public void parses() {
    assertParses("COLLAPSIBLE(LIST(TEXT=title),LIST(TEXT=content,NEW_LINE=\n))", "!* title\ncontent\n*!\n");
  }

  @Test public void translates() {
    assertTranslates(collapsible(""), "!* title\ncontent\n*!\n");
    assertTranslates(collapsible(" closed"), "!*> title\ncontent\n*!\n");
    assertTranslates(collapsible(" invisible"), "!*< title\ncontent\n*!\n");
  }

  private String collapsible(String state) {
    return "<div class=\"collapsible" + state +"\">\n" +
    "\t<ul>\n" +
    "\t\t<li>\n" +
    "\t\t\t<a href=\"#\" class=\"expandall\">Expand</a>\n" +
    "\t\t</li>\n" +
    "\t\t<li>\n" +
    "\t\t\t<a href=\"#\" class=\"collapseall\">Collapse</a>\n" +
    "\t\t</li>\n" +
    "\t</ul>\n" +
    "\t<p class=\"title\">title</p>\n" +
    "\t<div>content<br/></div>\n" +
    "</div>\n";
  }

}
