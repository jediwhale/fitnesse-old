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
    assertParses("TEXT=stuff", "!-stuff-!");
  }

  @Test
  public void parsesLiteralError() {
    assertParses("TEXT=stuff,ERROR=Missing terminator: -!", "!-stuff");
  }

  @Test
  public void parsesNesting() {
    assertParses("ITALIC(ITALIC(TEXT=stuff))", "''!(''stuff'')!''");
  }

  @Test
  public void parsesPath() {
    assertParses("PATH(TEXT=stuff)", "!path stuff");
  }

  @Test
  public void parsesPathWithLeadingAndTrailing() {
    assertParses("TEXT=some,PATH(TEXT=stuff),TEXT= ,TEXT=here", "some!path stuff here");
  }

  @Test
  public void parsesPreformat() {
    assertParses("PREFORMAT(TEXT=stuff)", "{{{stuff}}}");
  }

  @Test
  public void parsesSee() {
    assertParses("SEE(TEXT=stuff)", "!see stuff");
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
