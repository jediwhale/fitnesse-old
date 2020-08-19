package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlElement;

public class Table {
  public static Symbol parse(Parser parser) {
    Symbol result = new Symbol(SymbolType.TABLE);
    parser.advance();
    Symbol row = new Symbol(SymbolType.LIST);
    do {
      Symbol cell = parser.parseList(DELIMITER);
      row.add(cell);
    } while (!parser.peek(0).isEndOfLine());
    result.add(row);
    return result;
  }

  public static String translate(Symbol symbol, Translator translator) {
    return "<table>" + HtmlElement.endl +
      "\t<tr>" + HtmlElement.endl +
      translateRow(symbol.getChild(0), translator) +
      "\t</tr>" + HtmlElement.endl +
      "</table>" + HtmlElement.endl;
  }

  private static String translateRow(Symbol row, TranslateSymbol translator) {
    return row.translateChildren(child-> cell(child, translator));
  }

  private static String cell(Symbol cell, TranslateSymbol translator) {
    return "\t\t<td>" + translator.translate(cell) + "</td>" + HtmlElement.endl;
  }

  private static final Token DELIMITER = new Token(TokenType.CELL_DELIMITER);
}
