package fitnesse.wikitext.parser3;

import java.util.Arrays;

class Preformat {
  static void scan(Token token, TokenSource source) {
    source.use(PREFORMAT_TYPES, type -> type == TokenType.PREFORMAT_END);
  }

  static Symbol parse(Parser parser) {
    return parser.textType(SymbolType.TEXT).parseList(SymbolType.PREFORMAT, parser.advance());
  }

  private static final TokenTypes PREFORMAT_TYPES = new TokenTypes(
    Arrays.asList(
      TokenType.PREFORMAT_END,
      TokenType.NEW_LINE,
      TokenType.BLANK_SPACE
    ),
    Arrays.asList(KeywordType.TODAY)
  );
}
