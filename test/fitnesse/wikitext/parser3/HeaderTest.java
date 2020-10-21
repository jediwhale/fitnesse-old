package fitnesse.wikitext.parser3;

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
    external.id = 999;
    assertParses("HEADER(LIST(TEXT=stuff))[level=1,id=999]", "!1 stuff");
    assertParses("HEADER(LIST(TEXT=hi))[level=1,id=1000],TEXT=there", "!1 hi\nthere");
  }

  @Test public void translates() {
    external.id = 999;
    assertTranslates("<h1 id=\"999\">stuff</h1>\n", "!1 stuff");
  }
}
