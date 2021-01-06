package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class PreformatTest {
  @Test public void scans() {
    assertScans("PreformatStart={{{,Text=hi,PreformatEnd=}}}", "{{{hi}}}");
  }

  @Test  public void parses() {
    assertParses("PREFORMAT(TEXT=stuff)", "{{{stuff}}}");
  }

  @Test public void parsesNoWikiLink() {
    assertParses("PREFORMAT(TEXT=HiThere)", "{{{HiThere}}}");
  }

  @Test public void parsesToday() {
    assertParses("PREFORMAT(TODAY,NEW_LINE=\n)", "{{{!today\n}}}");
  }

  @Test public void translates() {
    assertTranslates("<pre>some stuff</pre>", "{{{some stuff}}}");
  }
}
