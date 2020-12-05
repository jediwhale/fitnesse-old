package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

import java.util.Optional;

class Table {

  static Symbol parseStandard(Parser parser) {
    Token token = parser.peek(0);
    if (token.getContent().contains("!")) {
      parser.pushTypes(TokenTypes.LITERAL_TABLE_TYPES);
    }
    else {
      parser.pushTypes(TokenTypes.STANDARD_TABLE_TYPES);
    }
    Symbol result = parseWithBarDelimiter(parser.peek(0).getContent().contains("!") ? parser.textType(SymbolType.TEXT) : parser);
    parser.popTypes();
    return result;
  }

  static Symbol parsePlain(Parser parser) {
    return parseWithCustomDelimiter(parser.textType(SymbolType.TEXT));
  }

  private static Symbol parseWithCustomDelimiter(Parser parser) {
    Symbol result = new BranchSymbol(SymbolType.TABLE);
    parser.advance();
    Optional<String> separator = parser.peek(0).isType(TokenType.TEXT)
      ? Optional.of(parser.advance().getContent())
      : Optional.empty();
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
    cell.add(Symbol.text(content));
    return cell;
  }

  private static Symbol parseWithBarDelimiter(Parser parser) {
    Symbol result = new BranchSymbol(SymbolType.TABLE);
    parser.advance();
    do {
      Symbol row = new BranchSymbol(SymbolType.LIST);
      do {
        Symbol cell = parser.parseList(SymbolType.LIST,
          new Terminator(type -> type == TokenType.CELL_DELIMITER || type == TokenType.TABLE_END, "|", ""));
        row.add(cell);
      } while (!isEndOfRow(parser.peek(-1)));
      result.add(row);
    } while (!isEndOfTable(parser.peek(-1)));
    return result;
  }

  private static boolean isEndOfRow(Token token) {
    return token.getContent().contains("\r") || token.getContent().contains("\n") || isEndOfTable(token);
  }

  private static boolean isEndOfTable(Token token) {
    return token.isType(TokenType.TABLE_END) || token.isType(TokenType.END);
  }

  static String translate(Symbol table, Translator translator) {
    return table.collectBranches(child -> row(child, translator), HtmlTag.name("table"), HtmlTag::add).html();
  }

  private static HtmlTag row(Symbol row, Translator translator) {
    return row.collectBranches(child -> cell(child, translator), HtmlTag.name("tr"), HtmlTag::add);
  }

  private static HtmlTag cell(Symbol cell, Translator translator) {
    return HtmlTag.name("td").body(new CellTranslator(translator).translate(cell).trim().replaceAll(LITERAL_DELIMITER, ""));
  }

  private static final String LITERAL_DELIMITER = String.valueOf(134);

  private static class CellTranslator implements Translator {
    CellTranslator(Translator translator) {
      this.translator = translator;
    }

    @Override
    public TranslateRule findRule(SymbolType symbolType) {
      return symbolType == SymbolType.LITERAL
        // to prevent literals from being trimmed in table cells
        ? (symbol, t) -> LITERAL_DELIMITER + translator.translate(symbol) + LITERAL_DELIMITER
        : translator.findRule(symbolType);
    }

    private final Translator translator;
  }
}
