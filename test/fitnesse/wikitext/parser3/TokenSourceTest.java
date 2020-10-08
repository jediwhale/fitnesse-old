package fitnesse.wikitext.parser3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fitnesse.wikitext.parser3.Helper.assertScans;
import static fitnesse.wikitext.parser3.Helper.assertScansWord;
import static org.junit.Assert.assertEquals;

public class TokenSourceTest {
  @Test public void scansContent() {
    assertScansTypes("", "");
    assertScansTypes("tokenOne=%", "%");
    assertScansTypes("tokenOne=%,tokenTwo=^^", "%^^");
    assertScansTypes("tokenTwo=^^,Text=hi", "^^hi");
    assertScansTypes("Text=hi,tokenTwo=^^", "hi^^");
    assertScansTypes("Text=hi", "hi");
    assertScansTypes("tokenTwo=^^,Text=hi,tokenOne=%", "^^hi%");
  }

  @Test public void scansCustomTypes() {
    assertScansTypes("tokenCustom=@=,Text=%text%,tokenEndCustom==@", "@=%text%=@");
    assertScansTypes("tokenOne=%,tokenCustom=@=,Text=%text%,tokenEndCustom==@,tokenOne=%", "%@=%text%=@%");
  }

  @Test public void peeksContent() {
    TokenSource source = new TokenSource("%^^@=hi=@there", types);
    assertEquals("tokenOne=%", source.peek(0).toString());
    assertEquals("Text=hi", source.peek(3).toString());
    assertEquals("tokenTwo=^^", source.peek(1).toString());

    source.take();
    assertEquals("Text=hi", source.peek(2).toString());
    assertEquals("End", source.peek(5).toString());
  }

  @Test public void keepsPrevious() {
    TokenSource source = new TokenSource("%^^", types);
    source.take();
    assertEquals("tokenOne=%", source.getPrevious().toString());
    source.take();
    assertEquals("tokenTwo=^^", source.getPrevious().toString());
  }

  @Test public void putsBack() {
    TokenSource source = new TokenSource("%^^", types);
    source.take();
    assertEquals("tokenTwo=^^", source.peek(0).toString());
    source.putBack();
    assertEquals("tokenOne=%", source.peek(0).toString());
    source.take();
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
  public void scansToday() {
    assertScansWord("!today", "Today");
  }

  private void assertScansTypes(String expected, String input) {
    StringBuilder result = new StringBuilder();
    TokenSource source = new TokenSource(input, types);
    while (true) {
      Token token = source.take();
      if (token.isType(TokenType.END)) break;
      if (result.length() > 0) result.append(",");
      result.append(token.toString());
    }
    assertEquals(expected, result.toString());
  }

  private static final TokenType tokenOne = new TokenType("tokenOne", "%");
  private static final TokenType tokenTwo = new TokenType("tokenTwo", "^^");
  private static final TokenType tokenEndCustom = new TokenType("tokenEndCustom", "=@");
  private static final TokenType tokenCustom = new TokenType("tokenCustom", "@=").useScan(tokenEndCustom);
  private static final List<TokenType> types = new ArrayList<>(Arrays.asList(tokenOne, tokenTwo, tokenCustom));
}
