package fitnesse.wikitext.parser3;

public class Link {
  public static Symbol parse(Parser parser) {
    Symbol result = parser.peek(0).asSymbol(SymbolType.LINK);
    parser.advance();
    parser.parseToTerminator(result, LINK_TERMINATOR);
    return result;
  }

  public static String translate(Symbol symbol, Translator translator) {
    String path = symbol.translateContent(translator);
    return makeLink(path, path, "");
  }

  public static String makeWikiLink(External external, String wikiPath, String trailer, String description) {
    String fullPath = external.fullPath(wikiPath);
    return external.exists(wikiPath)
      ? makeLink(description, fullPath + trailer, "")
      : description + makeLink("[?]", fullPath + "?edit&amp;nonExistent=true", "title=\"create page\"");
  }

  public static String makeLink(String description, String path, String attributes) {
    int files = path.indexOf("//files/");
    return Html.anchor((files < 0 ? path : path.substring(files + 2)), attributes, description);
  }

  private static final Terminator LINK_TERMINATOR = new Terminator(token -> !token.isType(TokenType.TEXT), "non-text");
}
