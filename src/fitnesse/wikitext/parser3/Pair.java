package fitnesse.wikitext.parser3;

public class Pair {
  public static ParseRule parse(SymbolType symbolType) {
    return parser -> {
      Token start = parser.peek(0);
      parser.advance();
      return parser.parseList(start).asType(symbolType);
    };
  }

  public static TranslateRule translate(String tag) {
    return (symbol, translator) ->
      "<" + tag + ">" +
        symbol.translateChildren(translator) +
        "</" + tag + ">";
  }

  public static TranslateRule translate(String tag1, String tag2) {
    return (symbol, translator) ->
      "<" + tag1 + "><" + tag2 + ">" +
        symbol.translateChildren(translator) +
        "</" + tag2 + "></" + tag1 + ">";
  }
}
