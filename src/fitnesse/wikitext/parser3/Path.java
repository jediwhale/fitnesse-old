package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Path {
  static void scan(Content content, TokenList tokens) {
    new Scanner(PATH_TOKENS, type -> type == TokenType.NEW_LINE, text -> TokenType.TEXT)
      .scan(content, tokens);
  }

  static Symbol parse(Parser parser) {
    Symbol source = new Symbol(SymbolType.SOURCE, parser.peek(0).getContent());
    parser.advance();
    Symbol result = parser.parseList(parser.peek(-1)).asType(SymbolType.PATH);
    result.addFirst(source);
    return result;
  }

  static String translate(Symbol symbol, Translator translator) {
    return "path"; //todo
  }

  private static final List<TokenType> PATH_TOKENS = new ArrayList<>(Arrays.asList(
    //evaluator start and end
    TokenType.LITERAL_START,
    TokenType.NEW_LINE,
    TokenType.PATH,
    TokenType.VARIABLE,
    TokenType.BRACE_END
  ));
}
