package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.ParsingPage;
import org.junit.Before;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class HeaderTest {
  @Test public void scans() {
    assertScans("Header=!1 ,Text=stuff", "!1 stuff");
    assertScans("Header=!2 ,Text=stuff", "!2 stuff");
    assertScans("Header=!3 ,Text=stuff", "!3 stuff");
    assertScans("Header=!4 ,Text=stuff", "!4 stuff");
    assertScans("Header=!5 ,Text=stuff", "!5 stuff");
    assertScans("Header=!6 ,Text=stuff", "!6 stuff");
  }

  @Test public void noScanNotAtStart() {
    assertScans("BlankSpace= ,Text=!4,BlankSpace= ,Text=stuff", " !4 stuff");
  }

  @Test public void parses() {
    for (int i = 0; i < 999; i++) page.nextId();
    assertParses("HEADER(LIST(TEXT=stuff))[id=999,level=1]", "!1 stuff", page);
    assertParses("HEADER(LIST(TEXT=hi))[id=1000,level=1],TEXT=there", "!1 hi\nthere", page);
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
