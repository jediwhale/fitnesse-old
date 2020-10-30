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
    return Symbol.make(SymbolType.ALIAS, description, link);
  }

  public static String translate(Symbol symbol, HtmlTranslator translator, External external) { //todo: this is pretty opaque
    if (symbol.getBranch(0).getBranch(0).getType() == SymbolType.WIKI_LINK) {
      return translator.translate(symbol.getBranch(0));
    }
    String link = translator
      .substitute(
        symbol.getBranch(1).getBranch(0).getType() == SymbolType.LINK ? SymbolType.LINK : SymbolType.WIKI_LINK,
        SymbolType.LITERAL)
      .translate(symbol.getBranch(1));
    String description = translator.translate(symbol.getBranch(0));
    return WikiPath.makeLink(link,
      (path, trailer) -> Link.makeWikiLink(external, path, trailer, description),
      path -> Link.makeLink(description, path, ""));
  }
}
