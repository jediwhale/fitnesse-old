package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlElement;
import org.junit.Before;
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
    assertParses("TABLE(LIST(LIST(LIST(LITERAL=a)),LIST(TEXT=b)))", "|!-a-!|b|");
    assertParses("TABLE(LIST(LIST(TEXT=a)),LIST(LIST(TEXT=b)))", "|a|\n|b|");
  }

  @Test public void parsesLiteral() {
    assertParses("TABLE(LIST(LIST(TEXT=PageOne)))","!|PageOne|");
    assertParses("TABLE(LIST(LIST(TEXT=''a'')))","!|''a''|");
  }

  //todo: should this be valid?
  //@Test public void parsesVariableInLiteral() {
  //  external.putVariable("x", "|a|\n''b''\n");
  //  assertParses("TABLE(LIST(LIST(LIST(TEXT=|,TEXT=a,TEXT=|\n,TEXT=''b'',NEW_LINE=\n))))","!|${x}|", external);
  //}

  @Test public void translates() {
    assertTranslates(table(row(cell("a"))), "|a|");
    assertTranslates(table(row(cell("a"))), "| a  |");
    assertTranslates(table(row(cell("a") + cell("b"))), "|a|b|");
    assertTranslates(table(row(cell("a") )+ row(cell("b"))), "|a|\n|b|");
    assertTranslates(table(row(cell("a") )+ row(cell("b"))), "|a|  \n|b|  \n");
  }

  @Test public void translatesLiteralCell() {
    assertTranslates(table(row(cell("something\n"))), "|!-something\n-! |\n");
  }

  @Test public void translatesVariableWithCellDelimiter() {
    external.putVariable("x", "a|b");
    assertTranslates(table(row(cell("a|b"))), "|${x}|\n", external);
    assertTranslates(table(row(cell("a|b"))), "!|${x}|\n", external);
  }

  @Test public void translatesVariableWithNestedTable() {
    external.putVariable("x", "|a|\n");
    external.putVariable("y", "|a|");
    assertTranslates(table(row(cell(innerTable(row(cell("a")))))), "|${x}|\n", external);
    assertTranslates(table(row(cell(innerTable(row(cell("a")))))), "|${y}|\n", external);
    assertTranslates(table(row(cell("|a|"))), "!|${x}|\n", external);
  }

  @Test public void translatesVariableInLiteralTable() {
    external.putVariable("hi", "there");
    assertTranslates(table(row(cell("there"))), "!|${hi}|", external);
  }

  @Test public void translatesPageNameInLiteralTable() {
    external.putVariable("hi", "PageOne");
    assertTranslates(table(row(cell("PageOne"))), "!|${hi}|", external);
  }

  @Test public void translatesLiteralInLiteralTable()  {
    assertTranslates(table(row(cell("abc"))), "!|a!-b-!c|");
  }

  @Test public void translatesMarkupInLiteralTable()  {
    assertTranslates(table(row(cell("&lt;hi&gt;"))), "!|<hi>|");
  }

  @Test public void translatesLinksInNoLinksTable()  {
    assertTranslates(table(row(cell("WikiWord"))), "^|WikiWord|");
    assertTranslates(table(row(cell("http://mysite.org"))), "^|http://mysite.org|");
  }

  @Test public void translatesTwoTables() {
    assertTranslates(table(row(cell("a"))) + table(row(cell("b"))), "|a|\n!|b|\n");
  }

  private static String innerTable(String row) {
    return "<table>" + HtmlElement.endl + row + "</table>";
  }

  private static String table(String row) {
    return innerTable(row)+ HtmlElement.endl;
  }

  private static String row(String cell) {
    return "\t<tr>" + HtmlElement.endl + cell + "\t</tr>" + HtmlElement.endl;
  }

  private static String cell(String content) {
    return "\t\t<td>" + content + "</td>" + HtmlElement.endl;
  }

  @Before
  public void SetUp() {
    external = makeExternal();
  }

  private FakeExternal external;
}
