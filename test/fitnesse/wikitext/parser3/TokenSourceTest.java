package fitnesse.wikitext.parser3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TokenSourceTest {
  @Test public void scansContent() {
    assertScans("", "");
    assertScans("tokenOne=%", "%");
    assertScans("tokenOne=%,tokenTwo=^^", "%^^");
    assertScans("tokenTwo=^^,Text=hi", "^^hi");
    assertScans("Text=hi,tokenTwo=^^", "hi^^");
    assertScans("Text=hi", "hi");
    assertScans("tokenTwo=^^,Text=hi,tokenOne=%", "^^hi%");
  }

  @Test public void scansCustomTypes() {
    assertScans("tokenCustom=@=,Text=%text%,tokenEndCustom==@", "@=%text%=@");
    assertScans("tokenOne=%,tokenCustom=@=,Text=%text%,tokenEndCustom==@,tokenOne=%", "%@=%text%=@%");
  }

  @Test public void peeksContent() {
    TokenSource source = new TokenSource(new Content("%^^@=hi=@there"), types);
    assertEquals("tokenOne=%", source.peek(0).toString());
    assertEquals("Text=hi", source.peek(3).toString());
    assertEquals("tokenTwo=^^", source.peek(1).toString());

    source.take();
    assertEquals("Text=hi", source.peek(2).toString());
    assertEquals("End", source.peek(5).toString());
  }

  @Test public void keepsPrevious() {
    TokenSource source = new TokenSource(new Content("%^^"), types);
    source.take();
    assertEquals("tokenOne=%", source.getPrevious().toString());
    source.take();
    assertEquals("tokenTwo=^^", source.getPrevious().toString());
  }

  @Test public void putsBack() {
    TokenSource source = new TokenSource(new Content("%^^"), types);
    source.take();
    assertEquals("tokenTwo=^^", source.peek(0).toString());
    source.putBack();
    assertEquals("tokenOne=%", source.peek(0).toString());
    source.take();
  }

  private void assertScans(String expected, String input) {
    StringBuilder result = new StringBuilder();
    TokenSource source = new TokenSource(new Content(input), types);
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
