package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

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

  public static String translate(Symbol table, TranslateSymbol<String> translator) {
    HtmlTag result = HtmlTag.name("table");
    table.visitChildren(child -> row(child, translator), result::add);
    return result.html();
  }

  private static HtmlTag row(Symbol row, TranslateSymbol<String> translator) {
    HtmlTag result = HtmlTag.name("tr");
    row.visitChildren(child -> cell(child, translator), result::add);
    return result;
  }

  private static HtmlTag cell(Symbol cell, TranslateSymbol<String> translator) {
    return HtmlTag.name("td").body(translator.translate(cell).trim());
  }

  private static final Token DELIMITER = new Token(TokenType.CELL_DELIMITER);
}
