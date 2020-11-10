package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.HeadingContentBuilder;
import fitnesse.wikitext.shared.Names;

class Headings {
  static Symbol parse(Parser parser) {
    Symbol result =  new TaggedSymbol(SymbolType.HEADINGS);
    parser.advance();
    if (parser.peek(0).isType(TokenType.BLANK_SPACE) && parser.peek(1).getContent().equals("-" + Names.STYLE)) {
      parser.advance();
      parser.advance();
      parser.advance(); //todo: check blankspace
      result.putProperty(Names.STYLE, parser.advance().getContent()); //todo: check valid value
    }
    return result;
  }

  static String translate(Symbol symbol, Symbol syntaxTree) {
    HeadingContentBuilder builder = new HeadingContentBuilder(symbol.findProperty(Names.STYLE, Names.DEFAULT_STYLE));
    syntaxTree.walkPreOrder(node -> {
      if (node.getType() == SymbolType.HEADER) {
        builder.htmlElements(node, extractTextFromHeaderLine(node));
      }
    });
    return builder.html();
  }

  private static String extractTextFromHeaderLine(Symbol headerLine) {
    final StringBuilder result = new StringBuilder();
    headerLine.walkPreOrder(node -> {
      if (node.getType() == SymbolType.TEXT || node.getType() == SymbolType.LITERAL) {
        result.append(node.getContent());
      }
    });
    return result.toString();
  }
}
