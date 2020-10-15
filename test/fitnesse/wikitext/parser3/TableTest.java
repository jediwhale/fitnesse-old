package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlElement;
import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class TableTest {
  @Test public void scans() {
    assertScans("TableStart=|,Text=a,TableEnd=|","|a|");
    assertScans("TableStart=|,Text=a,CellDelimiter=|,Text=b,TableEnd=|","|a|b|");
    assertScans("TableStart=!|,Text=a,TableEnd=|","!|a|");
    assertScans("TableStart=-|,Text=a,TableEnd=|","-|a|");
    assertScans("TableStart=-!|,Text=a,TableEnd=|","-!|a|");
    assertScans("Text=say,NewLine=\n,TableStart=|,Text=a,TableEnd=|","say\n|a|");
    assertScans("TableStart=|,Text=a,TableEnd=|\n,Text=there","|a|\nthere");
  }

  @Test public void scansLiteral() {
    assertScans("TableStart=!|,Text=''a'',TableEnd=|","!|''a''|");
  }

  @Test public void scansTrailingBlanks() {
    assertScans("TableStart=|,Text=a,CellDelimiter=|  \n|,Text=b,TableEnd=|","|a|  \n|b|");
    assertScans("TableStart=|,Text=a,CellDelimiter=|\n|,Text=b,TableEnd=|  \n","|a|\n|b|  \n");
  }

  @Test public void parses() {
    assertParses("TABLE(LIST(LIST(TEXT=a)))", "|a|");
    assertParses("TEXT=say,NEW_LINE=\n,TABLE(LIST(LIST(TEXT=a)))", "say\n|a|");
    assertParses("TABLE(LIST(LIST(TEXT=a))),TEXT=there", "|a|\nthere");
    assertParses("TABLE(LIST(LIST(TEXT=a),LIST(TEXT=b)))", "|a|b|");
    assertParses("TABLE(LIST(LIST(TEXT=a),LIST(TEXT=b)),LIST(LIST(TEXT=c),LIST(TEXT=d)))", "|a|b|\n|c|d|");
    assertParses("TABLE(LIST(LIST(LIST(SOURCE=!-,LITERAL=a,SOURCE=-!)),LIST(TEXT=b)))", "|!-a-!|b|");
    assertParses("TABLE(LIST(LIST(TEXT=a)),LIST(LIST(TEXT=b)))", "|a|\n|b|");
  }

  @Test public void parsesLiteral() {
    assertParses("TABLE(LIST(LIST(TEXT=PageOne)))","!|PageOne|");
    assertParses("TABLE(LIST(LIST(TEXT=''a'')))","!|''a''|");
  }

  @Test public void parsesVariableInLiteral() {
    external.putVariable("x", "|a|\n''b''\n");
    assertParses("TABLE(LIST(LIST(LIST(TEXT=|,TEXT=a,TEXT=|\n,TEXT=''b'',NEW_LINE=\n))))","!|${x}|");
  }

  @Test public void translates() {
    assertTranslates(table(row(cell("a"))), "|a|");
    assertTranslates(table(row(cell("a"))), "| a  |");
    assertTranslates(table(row(cell("a") + cell("b"))), "|a|b|");
    assertTranslates(table(row(cell("a") )+ row(cell("b"))), "|a|\n|b|");
    assertTranslates(table(row(cell("a") )+ row(cell("b"))), "|a|  \n|b|  \n");
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
