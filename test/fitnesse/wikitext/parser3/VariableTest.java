package fitnesse.wikitext.parser3;

import org.junit.Before;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.assertParses;
import static fitnesse.wikitext.parser3.Helper.assertScans;
import static fitnesse.wikitext.parser3.Helper.assertTranslates;
import static org.junit.Assert.assertEquals;

public class VariableTest {

  @Test
  public void parsesPut() {
    assertParses("DEFINE(TEXT=x,TEXT=y)", "!define x {y}");
    assertParses("DEFINE(TEXT=x,TEXT=''y'')", "!define x {''y''}");
  }

  @Test
  public void translatesPut() {
    assertTranslatesDefine("x=y", "!define x {y}");
    assertTranslatesDefine("a.b_c=y", "!define a.b_c {y}");
    assertTranslatesDefine("x=''y''", "!define x {''y''}");
    assertTranslatesDefine("x=|a|\n|b|", "!define x {|a|\n|b|}");
  }

  @Test
  public void translatesPutErrors() {
    assertTranslates(Helper.toError("!define  Name must be alphanumeric") + "@x y", "!define @x y");
    assertTranslates(Helper.toError("!define x Missing blank space") + "{y}", "!define x{y}");
    assertTranslates(Helper.toError("!define x  Expected { ( or [") + "y", "!define x y");
  }

  @Test
  public void putsVariables() {
    Helper.parse("!define x {y}", external);
    assertEquals("y", external.findVariable("x").orElse(""));
  }

  @Test
  public void translatesPutNestedVariable() {
    external.putVariable("x", "b");
    assertTranslates(translateDefine("y=a${x}c") + " abc", "!define y (a${x}c) ${y}", external);
    assertTranslates(translateDefine("y=a${x}c") + " abc", "!define y {a${x}c} ${y}", external);
  }

  @Test
  public void scansGet() {
    assertScans("Text=*x*", "${x}");
    assertScans("Text=*xyz*", "${xyz}");
    assertScans("ExpressionStart=${=,Text=1+2,BraceEnd=}", "${=1+2}");
    assertScans("Text=$,BraceStart={,Text=xyz", "${xyz");
    assertScans("Text=$xyz,BraceEnd=}", "$xyz}");
    assertScans("Text=$,BraceStart={,BraceEnd=}", "${}");
  }

  @Test
  public void parsesGet() {
    external.putVariable("x", "y");
    assertParses("TEXT=y", "${x}", external);
  }

  @Test
  public void translatesGet() {
    external.putVariable("x", "y");
    assertTranslates("y", "${x}", external);
  }

  @Test
  public void translatesGetError() {
    assertTranslates(Helper.toError("Undefined variable: x"), "${x}", external);
  }

  @Before
  public void setUp() {
    external = Helper.makeExternal();
  }

  private void assertTranslatesDefine(String expected, String input) {
    assertTranslates(translateDefine(expected), input);
  }

  private String translateDefine(String expected) {
    return "<span class=\"meta\">variable defined: " + expected + "</span>";
  }

  private FakeExternal external;
}
