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
    assertScans("LiteralStart=!-,Text=HiThere,LiteralEnd=-!", "!-HiThere-!");
    assertScans("Text=say,LiteralStart=!-,Text=hi''there,LiteralEnd=-!,Text=now", "say!-hi''there-!now");
    assertScans("LiteralStart=!-,Text=hi,LiteralEnd=-!,CellDelimiter=|,Text=there", "!-hi-!|there");
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

  @Test public void scansHeader() {
    assertScansWord("!1", "Header");
    assertScansWord("!2", "Header");
    assertScansWord("!3", "Header");
    assertScansWord("!4", "Header");
    assertScansWord("!5", "Header");
    assertScansWord("!6", "Header");
  }

  @Test public void scansHeadings() { assertScansWord("!headings", "Headings"); }

  @Test public void scansImage() { assertScansWord("!img", "Image"); }

  @Test public void scansInclude() { assertScansWord("!include", "Include"); }

  @Test public void scansMeta() { assertScansWord("!meta", "Meta"); }

  @Test public void scansNote() { assertScansWord("!note", "Note"); }

  @Test public void scansPath() { assertScansWordAtStart("!path", "Path"); }

  @Test public void scansSee() { assertScansWord("!see", "See"); }

  @Test
  public void scansHorizontalRule() {
    assertScans("Strike=--,Text=-", "---");
    assertScans("HorizontalRule=----", "----");
    assertScans("Text=hi,HorizontalRule=----,Text=there", "hi----there");
    assertScans("HorizontalRule=-----", "-----");
    assertScans("HorizontalRule=--------", "--------");
  }

  @Test
  public void scansHashTable() {
    assertScans("HashTable=!{,Text=key1,Colon=:,Text=value1,Comma=,,Text=key2,Colon=:,Text=value2,BraceEnd=}",
      "!{key1:value1,key2:value2}");
  }

  @Test
  public void scansPlainTextTable() {
    assertScans("PlainTextTableStart=![,NewLine=\n,Text=hi,NewLine=\n,Text=there,NewLine=\n,PlainTextTableEnd=]!",
      "![\nhi\nthere\n]!");
  }

  @Test
  public void scansCollapsible() {
    assertScans("CollapsibleStart=!*,Text=stuff,CollapsibleEnd=*!", "!*stuff*!");
    assertScans("CollapsibleStart=!**,Text=stuff,CollapsibleEnd=**!", "!**stuff**!");
  }

  @Test
  public void scansContents() {
    assertScansWord("!contents", "Contents");
  }

  @Test
  public void scansHelp() {
    assertScansWord("!help", "Help");
  }

  @Test
  public void scansLastModified() {
    assertScansWord("!lastmodified", "LastModified");
  }

  @Test
  public void scansToday() {
    assertScansWord("!today", "Today");
  }

  private void assertScansWord(String word, String tokenType) {
    assertScans("Text=" + word + "hi", word + "hi");
    assertScans("Text=" + word + "!see", word + "!see");
    assertScans(tokenType + "=" + word + " ,Text=hi", word + " hi");
    assertScans("Text=hi," + tokenType + "=" + word + " ,Text=there", "hi" + word + " there");
    assertScans("Text=" + word.substring(0,1) + ",BlankSpace= ,Text=" + word.substring(1),
      word.substring(0,1) + " " + word.substring(1));
  }

  private void assertScansWordAtStart(String word, String tokenType) {
    assertScans("Text=" + word + "hi", word + "hi");
    assertScans("Text=" + word + "!see", word + "!see");
    assertScans(tokenType + "=" + word + " ,Text=hi", word + " hi");
    assertScans("Text=hi" + word + ",BlankSpace= ,Text=there", "hi" + word + " there");
    assertScans("Text=hi,NewLine=\n," + tokenType + "=" + word + " ,Text=there", "hi\n" + word + " there");
    assertScans("Text=" + word.substring(0,1) + ",BlankSpace= ,Text=" + word.substring(1),
      word.substring(0,1) + " " + word.substring(1));
  }
}
