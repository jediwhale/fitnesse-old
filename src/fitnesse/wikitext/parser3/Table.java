package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlElement;

public class Table {
  public static Symbol parse(Parser parser) {
    Symbol result = new Symbol(SymbolType.TABLE);
    do {
      parser.advance();
      Symbol row = new Symbol(SymbolType.LIST);
      do {
        Symbol cell = parser.parseList(DELIMITER);
        row.add(cell);
      } while (!parser.peek(0).isEndOfLine());
      result.add(row);
      parser.advance();
    } while (parser.peek(0).isType(TokenType.CELL_DELIMITER));
    return result;
  }

  public static String translate(Symbol symbol, Translator translator) {
    return "<table>" + HtmlElement.endl +
      symbol.translateChildren(child -> row(child, translator)) +
      "</table>" + HtmlElement.endl;
  }

  private static String row(Symbol row, TranslateSymbol translator) {
    return "\t<tr>" + HtmlElement.endl + row.translateChildren(child-> cell(child, translator)) + "\t</tr>" + HtmlElement.endl;
  }

  private static String cell(Symbol cell, TranslateSymbol translator) {
    return "\t\t<td>" + translator.translate(cell) + "</td>" + HtmlElement.endl;
  }

  private static final Token DELIMITER = new Token(TokenType.CELL_DELIMITER);
}
