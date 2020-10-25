package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class NoteTest {
  @Test public void scans() {
    assertScansWordAtStart("!note", "Note");
  }

  @Test public void parses() {
    assertParses("NOTE(LIST(TEXT=stuff))", "!note stuff");
  }

  @Test public void translates() {
    assertTranslates("<p class=\"note\">stuff</p>\n", "!note stuff");
  }
}
