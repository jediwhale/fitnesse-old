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
    TokenSource source = makeTokenSource("%^^@=hi=@there");
    assertEquals("tokenOne=%", source.peek(0).toString());
    assertEquals("Text=hi", source.peek(3).toString());
    assertEquals("tokenTwo=^^", source.peek(1).toString());

    source.take();
    assertEquals("Text=hi", source.peek(2).toString());
    assertEquals("End", source.peek(5).toString());
  }

  @Test public void keepsPrevious() {
    TokenSource source = makeTokenSource("%^^");
    source.take();
    assertEquals("tokenOne=%", source.getPrevious().toString());
    source.take();
    assertEquals("tokenTwo=^^", source.getPrevious().toString());
  }

  @Test public void putsBack() {
    TokenSource source = makeTokenSource("%^^");
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
  public void scansLeadingAndTrailingText() {
    assertScans("Text=hi,Bold=''',Text=there", "hi'''there");
  }

  @Test public void scansMeta() { assertScansWord("!meta", "Meta"); }

  @Test
  public void scansHorizontalRule() {
    assertScans("Strike=--,Text=-", "---");
    assertScans("HorizontalRule=----", "----");
    assertScans("Text=hi,HorizontalRule=----,Text=there", "hi----there");
    assertScans("HorizontalRule=-----", "-----");
    assertScans("HorizontalRule=--------", "--------");
  }


  private TokenSource makeTokenSource(String input) {
    return new TokenSource(new Content(input, Helper.makeParsingPage()), types);
  }

  private void assertScansTypes(String expected, String input) {
    StringBuilder result = new StringBuilder();
    TokenSource source = makeTokenSource(input);
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
  private static final TokenTypes types = new TokenTypes(Arrays.asList(tokenOne, tokenTwo, tokenCustom));
}
