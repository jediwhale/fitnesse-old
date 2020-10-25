package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class PreformatTest {
  @Test public void scans() {
    assertScans("PreformatStart={{{,Text=hi,PreformatEnd=}}}", "{{{hi}}}");
  }

  @Test  public void parses() {
    assertParses("LIST(SOURCE={{{,PREFORMAT=stuff,SOURCE=}}})", "{{{stuff}}}");
  }

  @Test public void parsesNoWikiLink() {
    assertParses("LIST(SOURCE={{{,PREFORMAT=HiThere,SOURCE=}}})", "{{{HiThere}}}");
  }

  @Test public void translates() {
    assertTranslates("<pre>some stuff</pre>", "{{{some stuff}}}");
  }
}
