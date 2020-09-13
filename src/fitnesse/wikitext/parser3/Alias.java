package fitnesse.wikitext.parser3;

public class Alias {
  public static Symbol parse(Parser parser) {
    Token start = parser.peek(0);
    parser.advance();
    final Symbol description = parser.parseList(start);
    if (description.hasError()) return description;
    if (!description.hasChild(0)) {
      parser.move(-1);
      return Symbol.error(start.getContent() + " Empty description");
    }
    Token middle = parser.peek(-1);
    final Symbol link = parser.parseList(middle);
    if (link.hasError()) {
      return Symbol.makeList(Symbol.error(start.getContent()), description, link);
    }
    if (!link.hasChild(0)) {
      parser.move(-1);
      return Symbol.makeList(Symbol.error(start.getContent()), description, Symbol.error(middle.getContent() + " Empty link"));
    }
    return Symbol.make(SymbolType.ALIAS,
      Symbol.inputText(TokenType.ALIAS_START),
      description,
      Symbol.inputText(TokenType.ALIAS_MIDDLE),
      link,
      Symbol.inputText(TokenType.ALIAS_END));
  }

  public static String translate(Symbol symbol, Translator translator) {
    if (symbol.getChild(1).getChild(0).getType() == SymbolType.WIKI_LINK) {
      return translator.translate(symbol.getChild(1));
    }
    SymbolType substitute = symbol.getChild(3).getChild(0).getType() == SymbolType.LINK
      ? SymbolType.LINK
      : SymbolType.WIKI_LINK;
    String link = translator.copy()
      .substitute(substitute, SymbolType.TEXT)
      .translate(symbol.getChild(3));
    String description = translator.translate(symbol.getChild(1));
    return WikiPath.makeLink(link,
      (path, trailer) -> Link.makeWikiLink(translator.getExternal(), path, trailer, description),
      path -> Link.makeLink(description, path, ""));
  }
}
