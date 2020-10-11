package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

class Table {
  static void scan(Token token, TokenSource source) {
    if (token.getContent().contains("!")) {
      source.use(TokenTypes.LITERAL_TABLE_TYPES, type -> type == TokenType.NEW_LINE);
    }
  }

  static Symbol parseStandard(Parser parser) {
    return parseWithBarDelimiter(parser.peek(0).getContent().contains("!") ? parser.textType(SymbolType.TEXT) : parser);
  }

  static Symbol parsePlain(Parser parser) {
    return parseWithCustomDelimiter(parser.textType(SymbolType.TEXT));
  }

  private static Symbol parseWithCustomDelimiter(Parser parser) {
    Symbol result = new Symbol(SymbolType.TABLE);
    parser.advance();
    do {
      Symbol row = new Symbol(SymbolType.LIST);
      parser.advance(); //todo: check it's newline
      Symbol cell = parser.parseList(DELIMITER);
      result.add(row);
    } while (!parser.peek(0).isType(TokenType.END) && !parser.peek(0).isType(TokenType.PLAIN_TEXT_TABLE_END));
    parser.advance();
    return result;
  }

  private static Symbol parseWithBarDelimiter(Parser parser) {
    Symbol result = new Symbol(SymbolType.TABLE);
    parser.advance();
    do {
      Symbol row = new Symbol(SymbolType.LIST);
      do {
        Symbol cell = parser.parseList(SymbolType.LIST, new Terminator(TokenType.CELL_DELIMITER));
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
