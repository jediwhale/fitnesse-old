package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertScans;

public class NestingTest {
  @Test public void scans() {
    assertScans("NestingStart=!(,Text=hi,NestingEnd=)!", "!(hi)!");
  }

  @Test public void parsesSimple() {
    assertParses("LIST(TEXT=hi)", "!(hi)!");
  }

  @Test public void parsesComplex() {
    assertParses("ITALIC(LIST(ITALIC(TEXT=stuff)))", "''!(''stuff'')!''");
  }

  @Test public void parsesNoTerminator() {
    assertParses("LIST(ERROR=!( Missing terminator: )!,TEXT=some,TEXT= ,TEXT=more)", "!(some more");
  }

  @Test public void parsesTableInTable() {
    assertParses("TABLE(LIST(LIST(LIST(TABLE(LIST(LIST(TEXT=a)))))))", "|!(|a|)!|");
  }
}
