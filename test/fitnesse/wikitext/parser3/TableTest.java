package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlElement;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class TableTest {
  @Test
  public void scans() {
    assertScans("CellDelimiter=|,Text=a,CellDelimiter=|","|a|");
  }

  @Test
  public void parses() {
    assertParses("TABLE(LIST(LIST(TEXT=a)))", "|a|");
  }

  @Test
  public void translates() {
    assertTranslates(table(row(cell("a"))), "|a|");
  }

  private static String table(String row) {
    return "<table>" + HtmlElement.endl + row + "</table>" + HtmlElement.endl;
  }

  private static String row(String cell) {
    return "\t<tr>" + HtmlElement.endl + cell + "\t</tr>" + HtmlElement.endl;
  }

  private static String cell(String content) {
    return "\t\t<td>" + content + "</td>" + HtmlElement.endl;
  }
}
