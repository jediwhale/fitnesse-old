package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertScans;

public class CommentTest {
  @Test
  public void scans() {
    assertScans("Comment=#comment", "#comment");
    assertScans("Comment=#comment\n,Text=more", "#comment\nmore");
    assertScans("Comment=#comment\r,Text=more", "#comment\rmore");
    assertScans("Comment=#comment\r\n,Text=more", "#comment\r\nmore");
    assertScans("Text=hi,NewLine=\n,Comment=#comment\n,Text=there", "hi\n#comment\nthere");
    assertScans("Text=not#comment", "not#comment");
  }

  @Test
  public void parses() {
    assertParses("TEXT", "#comment");
    assertParses("TEXT=hi,NEW_LINE=\n,TEXT,TEXT=there", "hi\n#comment\nthere");
  }

  @Test public void parsesTableAfterComment() {
    assertParses("TEXT,TABLE(LIST(LIST(TEXT=a)))", "#comment\n|a|");
  }
}
