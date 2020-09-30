package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;

public class ParserTest {
  @Test
  public void parsesEmpty() {
    assertParses("", "");
  }

  @Test
  public void parsesPreformat() {
    assertParses("LIST(SOURCE={{{,TEXT=stuff,SOURCE=}}})", "{{{stuff}}}");
  }

  @Test
  public void parsesText() {
    assertParses("TEXT=hi", "hi");
  }

  @Test
  public void parsesWhiteSpace() {
    assertParses("TEXT=hi,TEXT= ,TEXT=there", "hi there");
  }
}
