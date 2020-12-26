package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.ParsingPage;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertScans;
import static fitnesse.wikitext.parser3.Helper.assertTranslates;

public class LinkTest {
  @Test
  public void scans() {
    assertScans("Link=http://,Text=somewhere.com", "http://somewhere.com");
    assertScans("Link=https://,Text=else.ca", "https://else.ca");
    assertScans("Text=http:/somewhere.com", "http:/somewhere.com");
    assertScans("Text=httpx:/else.ca", "httpx:/else.ca");
  }

  @Test
  public void parses() {
    assertParses("LINK=http://(TEXT=somewhere.com)", "http://somewhere.com");
    assertParses("LINK=http://(TEXT=PageOne)", "http://PageOne");
  }

  @Test public void parsesWithVariable() {
    ParsingPage page = Helper.makeParsingPage();
    page.putVariable("x", "localhost:8080");
    assertParses("LINK=http://(TEXT=localhost:8080/page)", "http://${x}/page", page);
  }

  @Test
  public void translates() {
    assertTranslates(Html.anchor("http://somewhere.com"), "http://somewhere.com");
    assertTranslates(Html.anchor("files/myfile", "http://files/myfile"), "http://files/myfile");
  }

  @Test public void translatesWithVariable() {
    ParsingPage page = Helper.makeParsingPage();
    page.putVariable("x", "localhost:8080");
    assertTranslates(Html.anchor("http://localhost:8080/page"), "http://${x}/page", page);
  }

}
