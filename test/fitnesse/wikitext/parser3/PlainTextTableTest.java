package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertScans;

public class PlainTextTableTest {
  @Test public void scans() {
    assertScans("PlainTextTableStart=![,NewLine=\n,Text=hi,NewLine=\n,Text=there,NewLine=\n,PlainTextTableEnd=]!",
      "![\nhi\nthere\n]!");
  }

  @Test public void parsesSingleColumn() {
    assertParses("TABLE(LIST(LIST(TEXT=hi)))", "![\nhi\n]!");
    assertParses("TABLE(LIST(LIST(TEXT=hi)),LIST(LIST(TEXT=there)))", "![\nhi\nthere\n]!");
  }

  @Test public void parsesMultipleColumns() {
    assertParses("TABLE(LIST(LIST(TEXT=hi),LIST(TEXT=there)))", "![:\nhi:there\n]!");
  }

  @Test public void parsesHiddenRow() {
    assertParses("TABLE(LIST(LIST(TEXT=hi)),LIST(LIST(TEXT=there)))", "![ hi\nthere\n]!");
  }
}
