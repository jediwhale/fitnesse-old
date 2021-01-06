package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.Arrays;

public class Alias {
  static void scan(Token token, TokenSource source) {
    source.use(ALIAS_TYPES, type -> type == TokenType.ALIAS_END);
  }

  public static Symbol parse(Parser parser) {
    Token start = parser.advance();
    final Symbol description = parser.parseList(start);
    if (description.hasError()) return description;
    if (description.isLeaf()) {
      parser.putBack();
      return Symbol.error(start.getContent() + " Empty description");
    }
    parser.putBack();
    Token middle = parser.advance();
    final Symbol link = parser.textType(SymbolType.TEXT).parseList(middle);
    if (link.hasError()) {
      return Symbol.makeList(Symbol.error(start.getContent()), description, link);
    }
    if (link.isLeaf()) {
      parser.putBack();
      return Symbol.makeList(Symbol.error(start.getContent()), description, Symbol.error(middle.getContent() + " Empty link"));
    }
    if (description.getBranch(0).getType() == SymbolType.WIKI_LINK)
      return description.getBranch(0); // link is ignored if description is wikilink

    //todo: this is pretty wild...
    String linkContent = link.allContents();
    int linkPathLength = WikiPath.pagePathLength(linkContent);
    if (linkPathLength > 0) { //starts with a page path
      Symbol firstBranch = link.getBranch(0);
      String content = firstBranch.getContent();
      int length = WikiPath.pagePathLength(content);
      Symbol result = length > 0
        ? new BranchSymbol(SymbolType.WIKI_LINK, content.substring(0, length), firstBranch.getOffset())
        : new BranchSymbol(SymbolType.WIKI_LINK, "");
      result.add(Symbol.text(linkPathLength > length ? linkContent.substring(0, linkPathLength) : ""));
      result.add(description);
      if (linkPathLength < linkContent.length()) result.add(Symbol.text(linkContent.substring(linkPathLength)));
      return result;
    }
    return Symbol.make(SymbolType.LINK, link, description);
  }

  private static final TokenTypes ALIAS_TYPES = new TokenTypes(Arrays.asList(
    TokenType.VARIABLE_VALUE, //must be first
    TokenType.ALIAS_END, TokenType.EXPRESSION_START, TokenType.EXPRESSION_END, TokenType.BRACE_END
  ));
}
