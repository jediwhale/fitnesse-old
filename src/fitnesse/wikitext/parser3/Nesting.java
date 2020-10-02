package fitnesse.wikitext.parser3;

class Nesting {
  static Symbol parse(Parser parser) {
    return parser.parseList(parser.advance());
  }
}
