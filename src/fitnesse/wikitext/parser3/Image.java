package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.Names;

class Image {
  static Symbol parse(Parser parser) {
    Symbol result = new TaggedSymbol(SymbolType.IMAGE);
    String content = parser.advance().getContent();
    if (content.startsWith("!img-l")) {
      result.putProperty(Names.IMAGE_CLASS, Names.LEFT);
    } else if (content.startsWith("!img-r")) {
      result.putProperty(Names.IMAGE_CLASS, Names.RIGHT);
    }
    result.add(new LeafSymbol(SymbolType.SOURCE, content));
    result.add(parser.textType(SymbolType.TEXT).parseCurrent());
    return result;
  }
}
