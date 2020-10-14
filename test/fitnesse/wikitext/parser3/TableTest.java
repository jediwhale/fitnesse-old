package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlElement;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class TableTest {
  @Test public void scans() {
    assertScans("Table=|,Text=a,CellDelimiter=|","|a|");
    assertScans("Table=!|,Text=a,CellDelimiter=|","!|a|");
    assertScans("Table=-|,Text=a,CellDelimiter=|","-|a|");
    assertScans("Table=-!|,Text=a,CellDelimiter=|","-!|a|");
    assertScans("Text=say,NewLine=\n,Table=|,Text=a,CellDelimiter=|","say\n|a|");
  }

  @Test public void scansLiteral() {
    assertScans("Table=!|,Text=''a'',CellDelimiter=|","!|''a''|");
  }

  @Test public void scansTrailingBlanks() {
    assertScans("Table=|,Text=a,CellDelimiter=|  \n|,Text=b,CellDelimiter=|","|a|  \n|b|");
    assertScans("Table=|,Text=a,CellDelimiter=|\n|,Text=b,CellDelimiter=|  ,NewLine=\n","|a|\n|b|  \n");
  }

  @Test public void parses() {
    assertParses("TABLE(LIST(LIST(TEXT=a)))", "|a|");
    assertParses("TEXT=say,TEXT=\n,TABLE(LIST(LIST(TEXT=a)))", "say\n|a|");
    assertParses("TABLE(LIST(LIST(TEXT=a),LIST(TEXT=b)))", "|a|b|");
    assertParses("TABLE(LIST(LIST(TEXT=a),LIST(TEXT=b)),LIST(LIST(TEXT=c),LIST(TEXT=d)))", "|a|b|\n|c|d|");
    assertParses("TABLE(LIST(LIST(LIST(SOURCE=!-,LITERAL=a,SOURCE=-!)),LIST(TEXT=b)))", "|!-a-!|b|");
    assertParses("TABLE(LIST(LIST(TEXT=a)),LIST(LIST(TEXT=b)))", "|a|\n|b|");
  }

  @Test public void parsesLiteral() {
    assertParses("TABLE(LIST(LIST(TEXT=PageOne)))","!|PageOne|");
  }

  @Test public void translates() {
    assertTranslates(table(row(cell("a"))), "|a|");
    assertTranslates(table(row(cell("a"))), "| a  |");
    assertTranslates(table(row(cell("a") + cell("b"))), "|a|b|");
    assertTranslates(table(row(cell("a") )+ row(cell("b"))), "|a|\n|b|");
    assertTranslates(table(row(cell("a") )+ row(cell("b"))) + "\n", "|a|  \n|b|  \n");
  }

  @Test public void translatesVariableInLiteralTable() {
    external.putVariable("hi", "there");
    assertTranslates(table(row(cell("there"))), "!|${hi}|");
  }

  @Test public void translatesPageNameInLiteralTable() {
    external.putVariable("hi", "PageOne");
    assertTranslates(table(row(cell("PageOne"))), "!|${hi}|");
  }

  @Test public void translatesLiteralInLiteralTable()  {
    assertTranslates(table(row(cell("abc"))), "!|a!-b-!c|");
  }

  @Test public void translatesMarkupInLiteralTable()  {
    assertTranslates(table(row(cell("&lt;hi&gt;"))), "!|<hi>|");
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
