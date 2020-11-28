package fitnesse.wikitext.parser3;

import org.junit.Before;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertScans;
import static fitnesse.wikitext.parser3.Helper.assertTranslates;

public class ExpressionTest {

  @Test public void scans() {
    assertScans("ExpressionStart=${=,Text=1+2,ExpressionEnd==}", "${=1+2=}");
  }

  @Test public void parses() {
    assertParses("EXPRESSION(LIST(TEXT=1+2))", "${=1+2=}");
  }

  @Test public void translates() {
    assertTranslates("3", "${=1+2=}");
  }

  @Test public void translatesWithVariable() {
    external.putVariable("x", "1");
    assertTranslates("3", "${=${x}+2=}", external);
  }

  @Test public void translatesWithLocale() {
    external.putVariable("FORMAT_LOCALE", "fr");
    assertTranslates("0,5", "${=%1.1f:1/2=}", external);
  }

  @Before
  public void SetUp() {
    external = Helper.makeExternal();
  }

  private FakeExternal external;
}
