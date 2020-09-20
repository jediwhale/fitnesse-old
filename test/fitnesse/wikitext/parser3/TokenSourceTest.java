package fitnesse.wikitext.parser3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

  private void assertScans(String expected, String input) {
    StringBuilder result = new StringBuilder();
    TokenSource source = new TokenSource(new Content(input));
    source.use(types);
    while (true) {
      Token token = source.next();
      if (token.isType(TokenType.END)) break;
      if (result.length() > 0) result.append(",");
      result.append(token.toString());
    }
    assertEquals(expected, result.toString());
  }

  private static void customScan(TokenSource source) {
    source.use(customTypes, t -> t == tokenEndCustom);
  }

  private static final TokenType tokenOne = new TokenType("tokenOne", "%");
  private static final TokenType tokenTwo = new TokenType("tokenTwo", "^^");
  private static final TokenType tokenCustom = new TokenType("tokenCustom", "@=").useScan(TokenSourceTest::customScan);
  private static final TokenType tokenEndCustom = new TokenType("tokenEndCustom", "=@");
  private static final List<TokenType> types = new ArrayList<>(Arrays.asList(tokenOne, tokenTwo, tokenCustom));
  private static final List<TokenType> customTypes = new ArrayList<>(Collections.singletonList(tokenEndCustom));
}
