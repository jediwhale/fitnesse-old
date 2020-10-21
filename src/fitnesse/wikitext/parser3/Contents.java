package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.wikitext.VariableStore;
import fitnesse.wikitext.shared.ContentsItemBuilder;
import fitnesse.wikitext.shared.VariableName;

class Contents {
  static Symbol parse(Parser parser, VariableStore variables) {
    Symbol result = new TaggedSymbol(SymbolType.CONTENTS);
    do {
      Token next = parser.advance();
      String option = next.getContent();
      if (next.isType(TokenType.TEXT) && next.getContent().startsWith("-")) {
        if (option.equals("-R")) { //todo: dry
          result.putProperty(option, String.valueOf(Integer.MAX_VALUE));
        }
        else if (option.startsWith("-R")) {
          result.putProperty("-R", option.substring(2));
        }
        else {
          result.putProperty(option, "");
        }
      } //todo: check for invalid types
    }
    while (!parser.peek(-1).isEndOfLine());
    result.copyVariables(new String[] {
        VariableName.HELP_TOC,
        VariableName.HELP_INSTEAD_OF_TITLE_TOC,
        VariableName.REGRACE_TOC,
        VariableName.PROPERTY_TOC,
        VariableName.FILTER_TOC,
        VariableName.MORE_SUFFIX_TOC,
        VariableName.PROPERTY_CHARACTERS},
      variables);
    return result;
  }

  static String translate(Symbol symbol, External external) {
    HtmlTag contents = new HtmlTag("div").attribute("class", "contents"); //todo: dry
    contents.add(HtmlUtil.makeBold("Contents:"));
    ContentsItemBuilder itemBuilder = new ContentsItemBuilder(symbol, 1, external.getSourcePage());
    contents.add(itemBuilder.buildLevel(external.getSourcePage()));
    return contents.html();
  }
}
