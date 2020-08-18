package fitnesse.wikitext.parser3;

import static org.junit.Assert.assertEquals;

public class Helper {
  public static void assertScans(String expected, String input) {
    String result = expected + (expected.length() > 0 ? "," : "") + "End";
    assertEquals(input, result, scan(input).toString());
  }

  public static void assertParses(String expected, String input) {
    String result = "LIST" + (expected.length() > 0 ? "(" + expected + ")" : expected);
    assertEquals(input, result, parse(input).toString());
  }

  public static void assertTranslates(String expected, String input) {
    assertEquals(input, expected, new HtmlTranslator(external).translate(parse(input)));
  }

  public static Symbol parse(String input) {
    return Parser.parse(input, external);
  }

  public static String toError(String message) { return " <span class=\"error\">" + message + "</span> "; }

  public static FakeExternal external = new FakeExternal();

  private static TokenList scan(String input) {
    return new Scanner(input).scan();
  }
}
