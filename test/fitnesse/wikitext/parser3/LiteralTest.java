package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class LiteralTest {
  @Test
  public void scans() {
    assertScans("LiteralStart=!-,Text=hi'''there,LiteralEnd=-!", "!-hi'''there-!");
    assertScans("LiteralStart=!-,Text=HiThere,LiteralEnd=-!", "!-HiThere-!");
    assertScans("Text=say,LiteralStart=!-,Text=hi''there,LiteralEnd=-!,Text=now", "say!-hi''there-!now");
    assertScans("LiteralStart=!-,Text=hi,LiteralEnd=-!,CellDelimiter=|,Text=there", "!-hi-!|there");
    assertScans("LiteralStart=!-,Text=hi.there", "!-hi.there");
  }

  @Test
  public void parses() {
    assertParses("LIST(LITERAL=hi there)", "!-hi there-!");
    assertParses("LIST(LITERAL=HiThere)", "!-HiThere-!");
    assertParses("TEXT=some,LIST(LITERAL=stuff),TEXT=here", "some!-stuff-!here");
    assertParses("LIST(ERROR=!- Missing terminator: -!,LITERAL=stuff)", "!-stuff");
  }

  @Test public void translates() {
    assertTranslates("<i>hi</i>", "!-<i>hi</i>-!");
  }
}
