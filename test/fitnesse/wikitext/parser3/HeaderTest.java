package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.ParsingPage;
import org.junit.Before;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class HeaderTest {
  @Test public void scans() {
    assertScansKeyword("!1", "Header");
    assertScansKeyword("!2", "Header");
    assertScansKeyword("!3", "Header");
    assertScansKeyword("!4", "Header");
    assertScansKeyword("!5", "Header");
    assertScansKeyword("!6", "Header");
  }

  @Test public void parses() {
    for (int i = 0; i < 999; i++) page.nextId();
    assertParses("HEADER(LIST(TEXT=stuff))[id=999,level=1]", "!1 stuff", page);
    assertParses("HEADER(LIST(TEXT=hi))[id=1000,level=1],TEXT=there", "!1 hi\nthere", page);
    assertParses("TABLE(LIST(LIST(HEADER(LIST(TEXT=stuff))[id=0,level=4])))", "|!4 stuff|\n");
  }

  @Test public void translates() {
    for (int i = 0; i < 999; i++) page.nextId();
    assertTranslates("<h1 id=\"999\">stuff</h1>\n", "!1 stuff", page);
  }

  @Before
  public void SetUp() {
    page = Helper.makeParsingPage();
  }

  private ParsingPage page;
}
