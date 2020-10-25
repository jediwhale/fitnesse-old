package fitnesse.wikitext.parser3;

import java.util.function.Consumer;

class Path {

  static Symbol parse(Parser parser) {
    Symbol source = new LeafSymbol(SymbolType.SOURCE, parser.peek(0).getContent());
    parser.advance();
    Symbol result = parser.textType(SymbolType.TEXT).parseList(SymbolType.PATH, Terminator.END_LINE);
    result.addFirst(source);
    return result;
  }

  static void providePaths(Symbol node, Consumer<String> takePath) {
    StringBuilder path = new StringBuilder();
    node.walkPreOrder(child -> {
      if (child.getType() == SymbolType.TEXT) {
        path.append(child.getContent());
      }
    });
    takePath.accept(path.toString());
  }
}
