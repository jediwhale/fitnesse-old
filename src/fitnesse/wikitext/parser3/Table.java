package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

import java.util.Optional;

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
    Symbol result = new BranchSymbol(SymbolType.TABLE);
    parser.advance();
    Optional<String> separator = Optional.empty();
    if (parser.peek(0).isType(TokenType.TEXT)) {
      separator = Optional.of(parser.advance().getContent());
    }
    if (parser.peek(0).isType(TokenType.BLANK_SPACE)) {
      parser.advance();
      result.add(makeRow(parser, separator));
    }
    else {
      parser.advance(); //todo: check newline
    }
    do {
      result.add(makeRow(parser, separator));
    } while (!parser.peek(0).isType(TokenType.END) && !parser.peek(0).isType(TokenType.PLAIN_TEXT_TABLE_END));
    parser.advance();
    return result;
  }

  private static Symbol makeRow(Parser parser, Optional<String> separator) {
    Symbol row = new BranchSymbol(SymbolType.LIST);
    String rowText = parser.parseText(new Terminator(TokenType.NEW_LINE));
    if (separator.isPresent()) {
      for (String cellText : rowText.split(separator.get())) {
        row.add(makeCell(cellText));
      }
    }
    else {
      row.add(makeCell(rowText));
    }
    return row;
  }

  private static Symbol makeCell(String content) {
    Symbol cell = new BranchSymbol(SymbolType.LIST);
    cell.add(new LeafSymbol(SymbolType.TEXT, content));
    return cell;
  }

  private static Symbol parseWithBarDelimiter(Parser parser) {
    Symbol result = new BranchSymbol(SymbolType.TABLE);
    parser.advance();
    do {
      Symbol row = new BranchSymbol(SymbolType.LIST);
      do {
        Symbol cell = parser.parseList(SymbolType.LIST, new Terminator(TokenType.CELL_DELIMITER));
        row.add(cell);
      } while (!isEndOfRow(parser.peek(-1)) && !parser.peek(0).isEndOfTable()); //todo: clean up
      result.add(row);
    } while (!parser.peek(0).isEndOfTable());
    return result;
  }

  private static boolean isEndOfRow(Token token) {
    return token.getContent().contains("\r") || token.getContent().contains("\n");
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
}
