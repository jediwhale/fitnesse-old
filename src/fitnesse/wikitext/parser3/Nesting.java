package fitnesse.wikitext.parser3;

class Nesting {
  static final char START = 14;
  static final char END = 15;

  static Symbol parse(Parser parser) {
    return parser.parseList(parser.advance());
  }
}
