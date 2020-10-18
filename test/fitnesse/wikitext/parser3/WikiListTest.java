package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class WikiListTest {
  @Test public void scans() {
    assertScans("BulletList= *", " *");
    assertScans("BulletList=   *", "   *");
    assertScans("Text=hi,BlankSpace= ,Text=*", "hi *");
    assertScans("Text=hi,NewLine=\n,BulletList= *", "hi\n *");
  }

  @Test public void parses() {
    assertParses("WIKI_LIST= *(LIST(TEXT=hi))", " * hi\n");
    assertParses("WIKI_LIST= *(LIST(ITALIC(TEXT=hi),TEXT=there))", " * ''hi''there\n");
  }

  @Test public void parsesMultipleItems() {
    assertParses("WIKI_LIST= *(LIST(TEXT=hi),LIST(TEXT=there))", " * hi\n * there");
  }

  @Test public void translates() {
    assertTranslates("<ul>\n\t<li>hi</li>\n</ul>\n", " * hi");
  }
}
