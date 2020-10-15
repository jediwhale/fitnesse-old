package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class KeywordTest {
  @Test public void scansNote() {
    assertScans("Note=!note ,Text=something", "!note something");
  }

  @Test public void scansSee() {
    assertScans("See=!see ,Text=something", "!see something");
  }

  @Test public void parsesSee() {
    assertParses("SEE(WIKI_LINK=MyPage)", "!see MyPage");
  }

  @Test public void translatesSee() {
    assertTranslates("<b>See: <a href=\"Fake.MyPage\">MyPage</a></b>", "!see MyPage");
  }
}
