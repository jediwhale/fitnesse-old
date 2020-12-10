package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class LineTest {
  @Test public void scans() {
    assertScansWord("!note", "Note");
  }

  @Test public void parses() {
    assertParses("NOTE(LIST(TEXT=stuff))", "!note stuff");
    assertParses("TABLE(LIST(LIST(NOTE(LIST(TEXT=stuff)))))", "|!note stuff|\n");
    assertParses("DEFINE(TEXT=x,TEXT=!note stuff)", "!define x {!note stuff}");
    assertParses("DEFINE(TEXT=x,TEXT=!note !meta a)", "!define x {!note !meta a}");

  }

  @Test public void translates() {
    assertTranslates("<p class=\"note\">stuff</p>\n", "!note stuff");
    assertTranslates("<span class=\"meta\">stuff</span>", "!meta stuff");
  }
}
