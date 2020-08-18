package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlElement;

public class Table {
  public static Symbol parse(Parser parser) {
    Symbol result = new Symbol(SymbolType.TABLE);
    parser.advance();
    Symbol cell = parser.parseList(parser.peek(-1));
    Symbol row = new Symbol(SymbolType.LIST);
    row.add(cell);
    result.add(row);
    return result;
  }

  public static String translate(Symbol symbol, Translator translator) {
    return "<table>" + HtmlElement.endl +
      "\t<tr>" + HtmlElement.endl +
      "\t\t<td>" + translator.translate(symbol.getChild(0).getChild(0)) + "</td>" + HtmlElement.endl +
      "\t</tr>" + HtmlElement.endl +
      "</table>" + HtmlElement.endl;
  }
}
