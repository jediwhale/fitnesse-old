package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

class Table {
  static void scan(Token token, TokenSource source) {
    if (token.getContent().contains("!")) {
      source.use(TokenTypes.LITERAL_TABLE_TYPES, type -> type == TokenType.NEW_LINE);
    }
  }

  static Symbol parse(Parser parser) {
    return parseTable(parser.peek(0).getContent().contains("!") ? parser.textType(SymbolType.TEXT) : parser);
  }

  private static Symbol parseTable(Parser parser) {
    Symbol result = new Symbol(SymbolType.TABLE);
    parser.advance();
    do {
      Symbol row = new Symbol(SymbolType.LIST);
      do {
        Symbol cell = parser.parseList(DELIMITER);
        row.add(cell);
      } while (parser.peek(-1).getContent().equals("|") && !parser.peek(0).isEndOfTable()); //todo: clean up
      result.add(row);
    } while (!parser.peek(0).isEndOfTable());
    return result;
  }

  static String translate(Symbol table, TranslateSymbol<String> translator) {
    return table.collectChildren(child -> row(child, translator), HtmlTag.name("table"), HtmlTag::add).html();
  }

  private static HtmlTag row(Symbol row, TranslateSymbol<String> translator) {
    return row.collectChildren(child -> cell(child, translator), HtmlTag.name("tr"), HtmlTag::add);
  }

  private static HtmlTag cell(Symbol cell, TranslateSymbol<String> translator) {
    return HtmlTag.name("td").body(translator.translate(cell).trim());
  }

  private static final Token DELIMITER = new Token(TokenType.CELL_DELIMITER);
}
