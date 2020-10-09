package fitnesse.wikitext.parser3;

public class Alias {
  public static Symbol parse(Parser parser) {
    Token start = parser.advance();
    final Symbol description = parser.parseList(start);
    if (description.hasError()) return description;
    if (!description.hasChildren()) {
      parser.putBack();
      return Symbol.error(start.getContent() + " Empty description");
    }
    parser.putBack();
    Token middle = parser.advance();
    final Symbol link = parser.parseList(middle);
    if (link.hasError()) {
      return Symbol.makeList(Symbol.error(start.getContent()), description, link);
    }
    if (!link.hasChildren()) {
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

  public static String translate(Symbol symbol, Translator translator) { //todo: this is pretty opaque
    if (symbol.getChild(1).getChild(0).getType() == SymbolType.WIKI_LINK) {
      return translator.translate(symbol.getChild(1));
    }
    String link = translator
      .copy()
      .substitute(
        symbol.getChild(3).getChild(0).getType() == SymbolType.LINK ? SymbolType.LINK : SymbolType.WIKI_LINK,
        SymbolType.LITERAL)
      .translate(symbol.getChild(3));
    String description = translator.translate(symbol.getChild(1));
    return WikiPath.makeLink(link,
      (path, trailer) -> Link.makeWikiLink(translator.getExternal(), path, trailer, description),
      path -> Link.makeLink(description, path, ""));
  }
}
