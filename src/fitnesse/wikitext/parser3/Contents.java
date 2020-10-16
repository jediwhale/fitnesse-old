package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.wikitext.VariableStore;

class Contents {
  static Symbol parse(Parser parser, VariableStore variables) {
    Symbol result = new TaggedSymbol(SymbolType.CONTENTS);
    parser.advance();
    return result;
  }

  static String translate(Symbol symbol, Translator translator) {
    HtmlTag contents = new HtmlTag("div").attribute("class", "contents");
    contents.add(HtmlUtil.makeBold("Contents:"));
    return contents.html();
  }
}
