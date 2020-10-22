package fitnesse.wikitext.parser3;

public class Alias {
  public static Symbol parse(Parser parser) {
    Token start = parser.advance();
    final Symbol description = parser.parseList(start);
    if (description.hasError()) return description;
    if (description.isLeaf()) {
      parser.putBack();
      return Symbol.error(start.getContent() + " Empty description");
    }
    parser.putBack();
    Token middle = parser.advance();
    final Symbol link = parser.parseList(middle);
    if (link.hasError()) {
      return Symbol.makeList(Symbol.error(start.getContent()), description, link);
    }
    if (link.isLeaf()) {
      parser.putBack();
      return Symbol.makeList(Symbol.error(start.getContent()), description, Symbol.error(middle.getContent() + " Empty link"));
    }
    return Symbol.make(SymbolType.ALIAS,
      Symbol.source(TokenType.ALIAS_START),
      description,
      Symbol.source(TokenType.ALIAS_MIDDLE),
      link,
      Symbol.source(TokenType.ALIAS_END));
  }

  public static String translate(Symbol symbol, HtmlTranslator translator, External external) { //todo: this is pretty opaque
    if (symbol.getBranch(1).getBranch(0).getType() == SymbolType.WIKI_LINK) {
      return translator.translate(symbol.getBranch(1));
    }
    String link = translator
      .substitute(
        symbol.getBranch(3).getBranch(0).getType() == SymbolType.LINK ? SymbolType.LINK : SymbolType.WIKI_LINK,
        SymbolType.LITERAL)
      .translate(symbol.getBranch(3));
    String description = translator.translate(symbol.getBranch(1));
    return WikiPath.makeLink(link,
      (path, trailer) -> Link.makeWikiLink(external, path, trailer, description),
      path -> Link.makeLink(description, path, ""));
  }
}
