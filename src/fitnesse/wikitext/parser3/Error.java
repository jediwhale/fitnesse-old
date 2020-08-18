package fitnesse.wikitext.parser3;

public class Error {
  public static String translate(Symbol symbol, Translator translator) {
    return
      " <span class=\"error\">" +
        symbol.getContent() +
        symbol.translateChildren(translator) +
        "</span> ";
  }
}
