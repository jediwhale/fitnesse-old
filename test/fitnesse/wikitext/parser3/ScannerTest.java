package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertScans;

public class ScannerTest {
  @Test
  public void scansEmptyString() {
    assertScans("", "");
  }

  @Test
  public void scansText() {
    assertScans("Text=hi", "hi");
    assertScans("Text=hi.there", "hi.there");
    assertScans("Text=hi.there.", "hi.there.");
  }

  @Test
  public void scansBlankSpace() {
    assertScans("Text=hi,BlankSpace= ,Text=there", "hi there");
  }

  @Test
  public void scansMultipleBlankSpace() {
    assertScans("Text=hi,BlankSpace=\t \t,Text=there", "hi\t \tthere");
  }

  @Test
  public void scansLiteral() {
    assertScans("LiteralStart=!-,Text=hi'''there,LiteralEnd=-!", "!-hi'''there-!");
  }

  @Test
  public void scansLiteralWithLeadingAndTrailingText() {
    assertScans("Text=say,LiteralStart=!-,Text=hi''there,LiteralEnd=-!,Text=now", "say!-hi''there-!now");
  }

  @Test
  public void scansLiteralWithoutTerminator() {
    assertScans("LiteralStart=!-,Text=hi--there", "!-hi--there");
  }

  @Test
  public void scansNesting() {
    assertScans("NestingStart=!(,Text=hi,NestingEnd=)!", "!(hi)!");
  }

  @Test
  public void scansNewLine() {
    assertScans("Text=hi,NewLine=\r\n", "hi\r\n");
    assertScans("Text=hi,NewLine=\n", "hi\n");
    assertScans("Text=hi,NewLine=\r", "hi\r");
  }

  @Test
  public void scansNote() {
    assertScans("Note=!note ,Text=something", "!note something");
  }

  @Test
  public void scansPath() {
    assertScans("Path=!path ,Text=something", "!path something");
  }

  @Test
  public void scansPreformat() {
    assertScans("PreformatStart={{{,Text=hi'''there,PreformatEnd=}}}", "{{{hi'''there}}}");
  }

  @Test
  public void scansSee() {
    assertScans("See=!see ,Text=something", "!see something");
  }

  @Test
  public void scansLeadingAndTrailingText() {
    assertScans("Text=hi,Bold=''',Text=there", "hi'''there");
  }

}
