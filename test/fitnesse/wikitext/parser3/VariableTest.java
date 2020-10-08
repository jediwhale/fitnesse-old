package fitnesse.wikitext.parser3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static fitnesse.wikitext.parser3.Helper.*;

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
  }

  @Test
  public void translatesPutErrors() {
    assertTranslates(toError("!define  Name must be alphanumeric") + "@x y", "!define @x y");
    assertTranslates(toError("!define x Missing blank space") + "{y}", "!define x{y}");
    assertTranslates(toError("!define x  Expected { ( or [") + "y", "!define x y");
  }

  @Test
  public void putsVariables() {
    parse("!define x {y}");
    assertEquals("y", external.findVariable("x").orElse(""));
  }

  @Test
  public void translatesPutNestedVariable() {
    external.putVariable("x", "b");
    assertTranslates(translateDefine("y=a${x}c") + " abc", "!define y (a${x}c) ${y}");
    assertTranslates(translateDefine("y=a${x}c") + " abc", "!define y {a${x}c} ${y}");
  }

  @Test
  public void scansGet() {
    assertScans("Variable=${,Text=x,BraceEnd=}", "${x}");
  }

  @Test
  public void parsesGet() {
    external.putVariable("x", "y");
    assertParses("LIST(TEXT=y)", "${x}");
  }

  @Test
  public void translatesGet() {
    external.putVariable("x", "y");
    assertTranslates("y", "${x}");
  }

  @Test
  public void translatesGetError() {
    assertTranslates(toError("Undefined variable: x"), "${x}");
  }

  @Before
  public void setUp() {
    external = new FakeExternal();
  }

  private void assertTranslatesDefine(String expected, String input) {
    assertTranslates(translateDefine(expected), input);
  }

  private String translateDefine(String expected) {
    return "<span class=\"meta\">variable defined: " + expected + "</span>";
  }
}
