package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

class HashTable {
  static void scan(TokenSource source) {
    //todo: does this work with nested {...} ?
    source.use(TokenTypes.HASH_TABLE_TYPES, type -> type == DelimiterType.BRACE_END);
  }

  static Symbol parse(Parser parser) {
    Symbol result = new BranchSymbol(SymbolType.HASH_TABLE);
    parser.advance();
    do {
      Symbol row = new BranchSymbol(SymbolType.LIST);
      result.add(row);
      row.add(parser.parseList(SymbolType.LIST, KEY_TERMINATOR));
      row.add(parser.parseList(SymbolType.LIST, ROW_TERMINATOR));
    } while (parser.peek(-1).isType(DelimiterType.COMMA));
    return result;
  }

  static String translate(Symbol table, Translator translator) {
    return table.collectBranches(child -> row(child, translator), HtmlTag.name("table").attribute("class", "hash_table"), HtmlTag::add).html();
  }

  private static HtmlTag row(Symbol row, Translator translator) {
    HtmlTag result = HtmlTag.name("tr").attribute("class", "hash_row");
    result.add(cell(row.getBranch(0), "hash_key", translator));
    result.add(cell(row.getBranch(1), "hash_value", translator));
    return result;
  }

  private static HtmlTag cell(Symbol cell, String cellClass, Translator translator) {
    return HtmlTag.name("td").attribute("class", cellClass).body(translator.translate(cell).trim());
  }

  private static final Terminator KEY_TERMINATOR = new Terminator(type -> type == DelimiterType.COLON);
  private static final Terminator ROW_TERMINATOR = new Terminator(type -> type == DelimiterType.COMMA || type == DelimiterType.BRACE_END);
}
