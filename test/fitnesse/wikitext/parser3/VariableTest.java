package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.ParsingPage;
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
    assertParses("DEFINE(TEXT=x,TEXT=!define y (z))", "!define x {!define y (z)}");
    //todo: tests with nested define as literal text and with literal character?
    assertParses("LIST(DEFINE(TEXT=x,TEXT=y),ERROR=Missing terminator: } for !define)", "!define x {y");
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
    assertTranslates(translateDefine("x=y") + Helper.toError("Missing terminator: } for !define"), "!define x {y");
  }

  @Test
  public void putsVariables() {
    Helper.parse("!define x {y}", page);
    assertEquals("y", page.findVariable("x").orElse(""));
  }

  @Test
  public void translatesPutNestedVariable() {
    page.putVariable("x", "b");
    assertTranslates(translateDefine("y=a${x}c") + " abc", "!define y (a${x}c) ${y}", page);
    assertTranslates(translateDefine("y=a${x}c") + " abc", "!define y {a${x}c} ${y}", page);
  }

  @Test
  public void scansGet() {
    ParsingPage page = Helper.makeParsingPage();
    page.putVariable("x", "value");
    assertScans("Text=value", "${x}", page);
    page.putVariable("xyz", "other value");
    assertScans("Text=other,BlankSpace= ,Text=value", "${xyz}", page);
    assertScans("ExpressionStart=${=,Text=1+2,BraceEnd=}", "${=1+2}");
    assertScans("Text=$,BraceStart={,Text=xyz", "${xyz");
    assertScans("Text=$xyz,BraceEnd=}", "$xyz}");
    assertScans("Text=$,BraceStart={,BraceEnd=}", "${}");
  }

  @Test
  public void parsesGet() {
    page.putVariable("x", "y");
    assertParses("TEXT=y", "${x}", page);
  }

  @Test
  public void translatesGet() {
    page.putVariable("x", "y");
    assertTranslates("y", "${x}", page);
  }

  @Test
  public void translatesGetError() {
    assertTranslates(Helper.toError("Undefined variable: x"), "${x}", page);
  }

  @Before
  public void setUp() {
    page = Helper.makeParsingPage();
  }

  private void assertTranslatesDefine(String expected, String input) {
    assertTranslates(translateDefine(expected), input);
  }

  private String translateDefine(String expected) {
    return "<span class=\"meta\">variable defined: " + expected + "</span>";
  }

  private ParsingPage page;
}
