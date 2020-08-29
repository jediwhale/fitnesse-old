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
    assertScans("LiteralStart=!-,Text=hi,Bold=''',Text=there,LiteralEnd=-!", "!-hi'''there-!");
    assertScans("Text=say,LiteralStart=!-,Text=hi,Italic='',Text=there,LiteralEnd=-!,Text=now", "say!-hi''there-!now");
    assertScans("LiteralStart=!-,Text=hi,LiteralEnd=-!,CellDelimiter=|,Text=there", "!-hi-!|there");
    assertScans("LiteralStart=!-,Text=hi,Strike=--,Text=there", "!-hi--there");
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
  public void scansPreformat() {
    assertScans("PreformatStart={{{,Text=hi,PreformatEnd=}}}", "{{{hi}}}");
  }

  @Test
  public void scansLeadingAndTrailingText() {
    assertScans("Text=hi,Bold=''',Text=there", "hi'''there");
  }

  @Test public void scansAnchorName() { assertScansWord("!anchor", "AnchorName"); }

  @Test public void scansCenterLine() { assertScansWord("!c", "CenterLine"); }

  @Test public void scansDefine() { assertScansWord("!define", "Define"); }

  @Test public void scansHeadings() { assertScansWord("!headings", "Headings"); }

  @Test public void scansImage() { assertScansWord("!img", "Image"); }

  @Test public void scansInclude() { assertScansWord("!include", "Include"); }

  @Test public void scansMeta() { assertScansWord("!meta", "Meta"); }

  @Test public void scansNote() { assertScansWord("!note", "Note"); }

  @Test public void scansPath() { assertScansWord("!path", "Path"); }

  @Test public void scansSee() { assertScansWord("!see", "See"); }

  private void assertScansWord(String word, String tokenType) {
    assertScans("Text=" + word + "hi", word + "hi");
    assertScans("Text=" + word + "!see", word + "!see");
    assertScans(tokenType + "=" + word + " ,Text=hi", word + " hi");
    assertScans("Text=hi," + tokenType + "=" + word + " ,Text=there", "hi" + word + " there");
    assertScans("Text=" + word.substring(0,1) + ",BlankSpace= ,Text=" + word.substring(1), word.substring(0,1) + " " + word.substring(1));
  }
}
