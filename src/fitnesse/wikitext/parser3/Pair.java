package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

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
      HtmlTag.name(tag).body(symbol.translateChildren(translator)).htmlInline();
  }

  public static TranslateRule translate(String tag1, String tag2) {
    return (symbol, translator) ->
      HtmlTag.name(tag1).child(HtmlTag.name(tag2).body(symbol.translateChildren(translator))).htmlInline();
  }
}
