package fitnesse.wikitext.parser3;

import java.util.Arrays;

class Preformat {
  static void scan(TokenSource source) {
    source.use(PREFORMAT_TYPES, type -> type == DelimiterType.PREFORMAT_END);
  }

  static Symbol parse(Parser parser) {
    return parser.textType(SymbolType.TEXT).parseList(SymbolType.PREFORMAT, parser.advance());
  }

  private static final TokenTypes PREFORMAT_TYPES = new TokenTypes(
    Arrays.asList(
      DelimiterType.PREFORMAT_END,
      DelimiterType.NEW_LINE,
      DelimiterType.BLANK_SPACE
    ),
    Arrays.asList(KeywordType.TODAY)
  );
}
