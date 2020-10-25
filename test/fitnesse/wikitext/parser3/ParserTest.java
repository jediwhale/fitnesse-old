package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertTranslates;

public class ParserTest {
  @Test
  public void parsesEmpty() {
    assertParses("", "");
  }

  @Test
  public void parsesText() {
    assertParses("TEXT=hi", "hi");
  }

  @Test public void translateText() {
    assertTranslates("hi", "hi");
    assertTranslates("&lt;hi&gt;", "<hi>");
    assertTranslates("!|$", "&bang;&bar;&dollar;");
  }

  @Test
  public void parsesBlankSpace() {
    assertParses("TEXT=hi,TEXT= ,TEXT=there", "hi there");
  }
}
