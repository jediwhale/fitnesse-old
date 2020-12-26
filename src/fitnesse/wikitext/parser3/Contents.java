package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.wikitext.shared.ContentsItemBuilder;
import fitnesse.wikitext.shared.Names;
import fitnesse.wikitext.shared.PageSources;
import fitnesse.wikitext.shared.VariableStore;

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
        Names.HELP_TOC,
        Names.HELP_INSTEAD_OF_TITLE_TOC,
        Names.REGRACE_TOC,
        Names.PROPERTY_TOC,
        Names.FILTER_TOC,
        Names.MORE_SUFFIX_TOC,
        Names.PROPERTY_CHARACTERS},
      variables);
    return result;
  }

  static String translate(Symbol symbol, PageSources sources) {
    HtmlTag contents = new HtmlTag("div").attribute("class", "contents"); //todo: dry
    contents.add(HtmlUtil.makeBold("Contents:"));
    ContentsItemBuilder itemBuilder = new ContentsItemBuilder(symbol, 1, sources.getPage());
    contents.add(itemBuilder.buildLevel(sources.getPage()));
    return contents.html();
  }
}
