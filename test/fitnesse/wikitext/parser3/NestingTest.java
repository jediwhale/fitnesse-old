package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertScans;

public class NestingTest {
  @Test
  public void scans() {
    assertScans("NestingStart=!(,Text=hi,NestingEnd=)!", "!(hi)!");
  }

  @Test
  public void parses() {
    assertParses("NESTING(TEXT=hi)", "!(hi)!");
    assertParses("ITALIC(NESTING(ITALIC(TEXT=stuff)))", "''!(''stuff'')!''");
  }
}
