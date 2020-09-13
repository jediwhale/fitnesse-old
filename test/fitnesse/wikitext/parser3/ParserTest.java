package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;

public class ParserTest {
  @Test
  public void parsesEmpty() {
    assertParses("", "");
  }

  @Test
  public void parsesLiteral() {
    assertParses("LIST(INPUT_TEXT=!-,TEXT=stuff,INPUT_TEXT=-!)", "!-stuff-!");
    assertParses("TEXT=some,LIST(INPUT_TEXT=!-,TEXT=stuff,INPUT_TEXT=-!),TEXT=here", "some!-stuff-!here");
  }

  @Test
  public void parsesLiteralError() {
    assertParses("LIST(ERROR=!- Missing terminator: -!,TEXT=stuff)", "!-stuff");
  }

  @Test
  public void parsesNesting() {
    assertParses("ITALIC(ITALIC(TEXT=stuff))", "''!(''stuff'')!''");
  }

  @Test
  public void parsesPreformat() {
    assertParses("LIST(INPUT_TEXT={{{,TEXT=stuff,INPUT_TEXT=}}})", "{{{stuff}}}");
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
