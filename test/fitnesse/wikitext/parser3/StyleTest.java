package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class StyleTest {
  @Test
  public void scansStyleWithBraces() {
    assertScans("Style=!style_,Text=hi,BraceStart={,Text=there,BraceEnd=}", "!style_hi{there}");
    assertScans("Style=!style_,Text=style,BraceStart={,Text=there,BraceEnd=}", "!style_style{there}");
    assertScans("Text=!style,BraceStart={,Text=there,BraceEnd=}", "!style{there}");
    assertScans("Style=!style_,BraceStart={,Text=there,BraceEnd=}", "!style_{there}");
    assertScans("Style=!style_,Text=hi,BraceStart={,Text=there,BraceEnd=},BraceEnd=}", "!style_hi{there}}");
  }

  @Test
  public void scansStyleWithBrackets() {
    assertScans("Style=!style_,Text=hi,BracketStart=[,Text=there,BracketEnd=]", "!style_hi[there]");
    assertScans("Style=!style_,Text=style,BracketStart=[,Text=there,BracketEnd=]", "!style_style[there]");
    assertScans("Text=!style,BracketStart=[,Text=there,BracketEnd=]", "!style[there]");
    assertScans("Style=!style_,BracketStart=[,Text=there,BracketEnd=]", "!style_[there]");
    assertScans("Style=!style_,Text=hi,BracketStart=[,Text=there,AliasEnd=]]", "!style_hi[there]]");
  }

  @Test
  public void scansStyleWithParentheses() {
    assertScans("Style=!style_,Text=hi,ParenthesisStart=(,Text=there,ParenthesisEnd=)", "!style_hi(there)");
    assertScans("Style=!style_,Text=style,ParenthesisStart=(,Text=there,ParenthesisEnd=)", "!style_style(there)");
    assertScans("Text=!style,ParenthesisStart=(,Text=there,ParenthesisEnd=)", "!style(there)");
    assertScans("Style=!style_,ParenthesisStart=(,Text=there,ParenthesisEnd=)", "!style_(there)");
    assertScans("Style=!style_,Text=hi,ParenthesisStart=(,Text=there,ParenthesisEnd=),ParenthesisEnd=)", "!style_hi(there))");
  }

  @Test
  public void parsesStyleWithBraces() {
    assertParses("STYLE(TEXT=hi,LIST(TEXT=there))", "!style_hi{there}");
  }

  @Test
  public void parsesStyleWithBrackets() {
    assertParses("STYLE(TEXT=hi,LIST(TEXT=there))", "!style_hi[there]");
  }

  @Test
  public void parsesStyleWithParentheses() {
    assertParses("STYLE(TEXT=hi,LIST(TEXT=there))", "!style_hi(there)");
  }

  @Test
  public void translatesStyle() {
    assertTranslates("<span class=\"myStyle\">wow zap</span>", "!style_myStyle(wow zap)");
    assertTranslates("<span class=\"myStyle\">wow zap</span>", "!style_myStyle[wow zap]");
    assertTranslates("<span class=\"myStyle\">wow zap</span>", "!style_myStyle{wow zap}");
    assertTranslates("well<span class=\"hi\">there</span>friend", "well!style_hi(there)friend");
    assertTranslates("<span class=\"hi\">)</span>", "!style_hi[)]");
    assertTranslates("<span class=\"red\"><span class=\"blue\">wow zap</span></span>", "!style_red(!style_blue(wow zap))");
  }

  @Test
  public void translatesStyleWithError() {
    assertTranslates(toError("!style_myStyle[ Missing terminator: ]") + "wow zap)", "!style_myStyle[wow zap)");
    assertTranslates(toError("!style_red{ Missing terminator: }") + "<span class=\"blue\">hi}</span>", "!style_red{!style_blue(hi})");
    assertTranslates("!style(wow zap)", "!style(wow zap)");
    assertTranslates(toError("!style_ Expected class name") + "(wow zap)", "!style_(wow zap)");
    assertTranslates(toError("!style_ Expected class name"), "!style_");
    assertTranslates(toError("!style_red Expected { ( or [") + "]stuff", "!style_red]stuff");
  }
}
