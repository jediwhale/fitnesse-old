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
    if (parser.peek(0).getContent().equals(Names.IMAGE_WIDTH)) {
      parser.advance();
      parser.advance(); // check blankspace
      result.putProperty(Names.IMAGE_WIDTH, parser.advance().getContent());
      parser.advance(); //check blankspace
    }
    result.add(parser.textType(SymbolType.TEXT).parseCurrent());
    return result;
  }
}
