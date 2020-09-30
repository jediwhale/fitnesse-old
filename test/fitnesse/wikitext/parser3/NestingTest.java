package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertScans;

public class NestingTest {
  @Test public void scans() {
    assertScans("NestingStart=!(,Text=hi,NestingEnd=)!", "!(hi)!");
  }

  @Test public void parsesSimple() {
    assertParses("NESTING(TEXT=hi)", "!(hi)!");
  }

  @Test public void parsesNested() {
    assertParses("ITALIC(NESTING(ITALIC(TEXT=stuff)))", "''!(''stuff'')!''");
  }

  @Test public void parsesTableInTable() {
    assertParses("TABLE(LIST(LIST(NESTING(TABLE(LIST(LIST(TEXT=a)))))))", "|!(|a|)!|");
  }
}
