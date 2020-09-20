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

  private static final TokenType tokenOne = new TokenType("tokenOne", "%");
  private static final TokenType tokenTwo = new TokenType("tokenTwo", "^^");
  private static final List<TokenType> types = new ArrayList<>(Arrays.asList(tokenOne, tokenTwo));
}
