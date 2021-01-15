package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.ParsingPage;
import fitnesse.wikitext.shared.VariableSource;

import static org.junit.Assert.assertEquals;

public class Helper {

  public static void assertScansKeyword(String word, String tokenType) {
    assertScans("Text=" + word + "hi", word + "hi");
    assertScans(tokenType + "=" + word + ",Text=!saw", word + "!saw");
    assertScans(tokenType + "=" + word + ",BlankSpace= ,Text=hi", word + " hi");
    assertScans("Text=hi," + tokenType + "=" + word + ",BlankSpace= ,Text=there", "hi" + word + " there");
    assertScans("Text=" + word.substring(0,1) + ",BlankSpace= ,Text=" + word.substring(1),
      word.substring(0,1) + " " + word.substring(1));
  }

  public static void assertScansWord(String word, String tokenType) {
    assertScans("Text=" + word + "hi", word + "hi");
    assertScans("Text=" + word + ",Text=!saw", word + "!saw");
    assertScans(tokenType + "=" + word + " ,Text=hi", word + " hi");
    assertScans("Text=hi," + tokenType + "=" + word + " ,Text=there", "hi" + word + " there");
    assertScans("Text=" + word.substring(0,1) + ",BlankSpace= ,Text=" + word.substring(1),
      word.substring(0,1) + " " + word.substring(1));
  }

  public static void assertScansWordAtStart(String word, String tokenType) {
    assertScans("Text=" + word + "hi", word + "hi");
    assertScans("Text=" + word + ",Text=!saw", word + "!saw");
    assertScans(tokenType + "=" + word + " ,Text=hi", word + " hi");
    assertScans("Text=hi,Text=" + word + ",BlankSpace= ,Text=there", "hi" + word + " there");
    assertScans("Text=hi,NewLine=\n," + tokenType + "=" + word + " ,Text=there", "hi\n" + word + " there");
    assertScans("Text=" + word.substring(0,1) + ",BlankSpace= ,Text=" + word.substring(1),
      word.substring(0,1) + " " + word.substring(1));
  }

  public static void assertScans(String expected, String input) {
    assertScans(expected, input, makeParsingPage());
  }

  public static void assertScans(String expected, String input, VariableSource variables) {
    String result = expected + (expected.length() > 0 ? "," : "") + "End";
    assertEquals(input, result, scan(input, variables));
  }

  public static void assertParses(String expected, String input) {
    assertParses(expected, input, makeParsingPage());
  }

  public static void assertParses(String expected, String input, ParsingPage page) {
    String result = "LIST" + (expected.length() > 0 ? "(" + expected + ")" : expected);
    assertEquals(input, result, parse(input, page).toString());
  }

  public static void assertTranslates(String expected, String input) {
    assertTranslates(expected, input, makeParsingPage());
  }

  public static void assertTranslates(String expected, String input, ParsingPage page) { //todo: deal with newlines, could be platform-specific
    Symbol syntaxTree = parse(input, page);
    assertEquals(input, expected, new HtmlTranslator(syntaxTree, page).translate(syntaxTree));
  }

  public static Symbol parse(String input) {
    return parse(input, makeParsingPage());
  }

  public static Symbol parse(String input, ParsingPage page) {
    return Parser.parse(input, page);
  }

  public static ParsingPage makeParsingPage() { return new ParsingPage(new FakeSourcePage());}

  public static String toError(String message) { return " <span class=\"fail\">" + message + "</span> "; }

  private static String scan(String input, VariableSource variables) {
    TokenSource source = new TokenSource(new Content(input, variables), TokenTypes.WIKI_PAGE_TYPES);
    StringBuilder result = new StringBuilder();
    do {
      Token token = source.take();
      if (result.length() > 0) result.append(",");
      result.append(token.toString());
    } while (source.getPrevious().getType() != TokenType.END);
    return result.toString();
  }
}
