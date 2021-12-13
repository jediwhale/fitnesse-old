package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.ParsingPage;
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
    assertParses("EXPRESSION(TEXT=1+2)", "${=1+2=}");
    assertParses("LIST(ERROR=${= Missing terminator: =},TEXT=1+2=)", "${=1+2=");
  }

  @Test public void translates() {
    assertTranslates("3", "${=1+2=}");
  }

  @Test public void translatesWithVariable() {
    page.putVariable("x", "1");
    assertTranslates("3", "${=${x}+2=}", page);
  }

  @Test public void translatesWithLocale() {
    page.putVariable("FORMAT_LOCALE", "fr");
    assertTranslates("0,5", "${=%1.1f:1/2=}", page);
  }

  @Before
  public void SetUp() {
    page = Helper.makeParsingPage();
  }

  private ParsingPage page;
}
