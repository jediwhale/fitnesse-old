package fitnesse.wikitext.parser3;

import java.util.function.Consumer;

class Path {

  static Symbol parse(Parser parser) {
    Symbol source = new Symbol(SymbolType.SOURCE, parser.peek(0).getContent());
    Symbol result = parser.textType(SymbolType.TEXT).parseList(SymbolType.PATH, parser.advance());
    result.addFirst(source);
    return result;
  }

  static String translate(Symbol symbol, Translator translator) {
    return "path"; //todo
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
