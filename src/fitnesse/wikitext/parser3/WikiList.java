package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

class WikiList {
  static Symbol parse(Parser parser) {
    Symbol result = new BranchSymbol(SymbolType.WIKI_LIST, parser.peek(0).getContent());
    TokenType listType = parser.peek(0).getType();
    int indent = indent(parser.peek(0));
    while (parser.peek(0).isType(listType) && indent(parser.peek(0)) >= indent) {
      if (indent(parser.peek(0)) > indent) {
        result.lastBranch().add(parse(parser));
      }
      else {
        parser.advance();
        if (parser.peek(0).isType(DelimiterType.BLANK_SPACE)) parser.advance();
        Symbol item = new BranchSymbol(SymbolType.LIST);
        while (!parser.peek(0).isEndOfLine()) item.add(parser.parseCurrent());
        parser.advance();
        result.add(item);
      }
    }
    return result;
  }

  static int indent(Token token) {
    return token.getContent().length() - 1;
  }

  static String translate(Symbol symbol, Translator translator) {
    String listType = symbol.getContent().contains("*") ? "ul" : "ol";
    return symbol.collectBranches(
      child -> new HtmlTag("li", translator.translate(child)), new HtmlTag(listType), HtmlTag::add)
      .html();
  }
}
