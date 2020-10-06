package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertScans;
import static fitnesse.wikitext.parser3.Helper.assertTranslates;

public class ExpressionTest {

  @Test public void scans() {
    assertScans("ExpressionStart=${=,Text=1+2,ExpressionStart==}", "${=1+2=}");
  }

  @Test public void translates() {
    assertTranslates("3", "${=1+2=}");
  }
}
