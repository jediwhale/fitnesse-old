package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;

public class PreformatTest {
  @Test public void parsesNoWikiLink() {
    assertParses("LIST(SOURCE={{{,TEXT=HiThere,SOURCE=}}})", "{{{HiThere}}}");
  }
}
