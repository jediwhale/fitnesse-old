package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertScans;

public class CommentTest {
  @Test
  public void scans() {
    assertScans("Comment=#,Text=comment", "#comment");
    assertScans("Text=hi,NewLine=\n,Comment=#,Text=comment,NewLine=\n,Text=there", "hi\n#comment\nthere");
    assertScans("Text=not#comment", "not#comment");
  }

  @Test
  public void parses() {
    assertParses("SOURCE=#comment", "#comment");
    assertParses("TEXT=hi,TEXT=\n,SOURCE=#comment\n,TEXT=there", "hi\n#comment\nthere");
  }
}
