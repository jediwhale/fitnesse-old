package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

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
  }

  @Test
  public void translates() {
    assertTranslates(Html.anchor("http://somewhere.com", "http://somewhere.com"), "http://somewhere.com");
    assertTranslates(Html.anchor("files/myfile", "http://files/myfile"), "http://files/myfile");
  }

}
