package fitnesse.wikitext.parser3;

class Comment {
  static Symbol parse(Parser parser) {
    parser.advance();
    return Symbol.text("");
  }
}
