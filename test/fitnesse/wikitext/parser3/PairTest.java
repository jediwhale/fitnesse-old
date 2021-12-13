package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class PairTest {
  @Test
  public void scansBold() {
    assertScans("Bold=''',Text=hi,Bold='''", "'''hi'''");
    assertScans("Bold=''',Text='hi,Bold=''',Text='", "''''hi''''");
    assertScans("Bold=''',Text='hi',BlankSpace= ,Bold='''", "''''hi' '''");
  }

  @Test
  public void scansBoldItalic() {
    assertScans("BoldItalic=''''',Text=hi,BoldItalic='''''", "'''''hi'''''");
  }

  @Test
  public void scansItalic() {
    assertScans("Italic='',Text=hi,Italic=''", "''hi''");
    assertScans("Italic='',BlankSpace= ,Text='hi',BlankSpace= ,Italic=''", "'' 'hi' ''");
  }

  @Test
  public void scansStrike() {
    assertScans("Strike=--,Text=hi,Strike=--", "--hi--");
    assertScans("Text=well,Strike=--,Text=hi,Strike=--,Text=there", "well--hi--there");
    assertScans("Strike=--,Text=-,BlankSpace= ,Text=-", "--- -");
  }

  @Test
  public void parsesBold() {
    assertParses("BOLD(TEXT=hi)", "'''hi'''");
    assertParses("LIST(ERROR=''' Missing terminator: ''',TEXT=hi)", "'''hi");
  }

  @Test
  public void parsesBoldItalic() {
    assertParses("BOLD_ITALIC(TEXT=hi)", "'''''hi'''''");
  }

  @Test
  public void parsesItalic() {
    assertParses("ITALIC(TEXT=hi)", "''hi''");
  }

  @Test
  public void parsesStrike() {
    assertParses("STRIKE(TEXT=hi)", "--hi--");
  }

  @Test
  public void translatesBold() {
    assertTranslates("<b>bold text</b>", "'''bold text'''");
    assertTranslates("say<b>hi</b>now", "say'''hi'''now");
    assertTranslates(toError("''' Missing terminator: '''") + "hi", "'''hi");
    assertTranslates(toError("''' Missing terminator: '''") + "'", "''''");
  }

  @Test
  public void translatesItalic() {
    assertTranslates("<i>italic text</i>", "''italic text''");
    assertTranslates("<i> 'italic text' </i>", "'' 'italic text' ''");
    assertTranslates(toError("'' Missing terminator: ''"), "''");
  }

  @Test
  public void translatesStrike() {
    assertTranslates("<strike>some text</strike>", "--some text--");
    assertTranslates("<strike>embedded-dash</strike>", "--embedded-dash--");
  }

  @Test
  public void translatesBoldItalic() {
    assertTranslates("<b><i>text</i></b>", "'''''text'''''");
    assertTranslates(toError("''''' Missing terminator: '''''"), "'''''");
  }
}
