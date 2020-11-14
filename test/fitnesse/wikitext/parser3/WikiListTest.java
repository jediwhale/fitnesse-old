package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class WikiListTest {
  @Test public void scansBullet() {
    assertScans("BulletList= *", " *");
    assertScans("BulletList=   *", "   *");
    assertScans("Text=hi,BlankSpace= ,Text=*", "hi *");
    assertScans("Text=hi,NewLine=\n,BulletList= *", "hi\n *");
  }

  @Test public void scansNumber() {
    assertScans("NumberedList= 0", " 0");
    assertScans("NumberedList=   9", "   9");
    assertScans("Text=hi,BlankSpace= ,Text=5", "hi 5");
    assertScans("Text=hi,NewLine=\n,NumberedList= 3", "hi\n 3");
  }

  @Test public void parsesBullet() {
    assertParses("WIKI_LIST= *(LIST(TEXT=hi))", " * hi\n");
    assertParses("WIKI_LIST= *(LIST(ITALIC(TEXT=hi),TEXT=there))", " * ''hi''there\n");
  }

  @Test public void parsesNumbered() {
    assertParses("WIKI_LIST= 1(LIST(TEXT=hi))", " 1 hi\n");
    assertParses("WIKI_LIST= 1(LIST(ITALIC(TEXT=hi),TEXT=there))", " 1 ''hi''there\n");
  }

  @Test public void parsesMultipleItems() {
    assertParses("WIKI_LIST= *(LIST(TEXT=hi),LIST(TEXT=there))", " * hi\n * there");
  }

  @Test public void parsesNestedItems() {
    assertParses("WIKI_LIST= *(LIST(TEXT=hi,WIKI_LIST=  *(LIST(TEXT=there))))", " * hi\n  * there\n");
  }

  @Test public void translatesBullet() {
    assertTranslates("<ul>\n\t<li>hi</li>\n</ul>\n", " * hi");
  }

  @Test public void translatesNumbered() {
    assertTranslates("<ol>\n\t<li>hi</li>\n</ol>\n", " 0 hi");
  }
}
