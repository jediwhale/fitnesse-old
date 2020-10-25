package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertTranslates;

public class EMailTest {
  @Test public void parses() {
    assertParses("EMAIL=my@word.com", "my@word.com");
  }

  @Test public void translates() {
    assertTranslates("<a href=\"mailto:my@word.com\">my@word.com</a>", "my@word.com");
  }
}
