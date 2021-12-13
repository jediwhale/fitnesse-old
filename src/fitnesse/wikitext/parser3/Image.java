package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.Names;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Image {
  static Symbol parse(Parser parser) {
    Symbol result = new BranchSymbol(SymbolType.IMAGE);
    String content = parser.advance().getContent();
    parser.advance(); //todo: check blank
    if (content.startsWith("!img-l")) {
      result.putProperty(Names.IMAGE_CLASS, Names.LEFT);
    } else if (content.startsWith("!img-r")) {
      result.putProperty(Names.IMAGE_CLASS, Names.RIGHT);
    }
    while (parser.peek(0).getContent().startsWith("-")) {
      String option = parser.advance().getContent();
      if (OPTIONS.contains(option)) {
          parser.advance(); //todo: check blankspace
          result.putProperty(option, parser.advance().getContent());
          parser.advance(); //todo: check blankspace
        }
        else parser.advance(); //todo: invalid option
    }
    result.add(Symbol.text(parser.textType(SymbolType.TEXT).parseCurrent().allContents()));
    return result;
  }

  private static final List<String> OPTIONS = new ArrayList<>(Arrays.asList(
      Names.IMAGE_BORDER, Names.IMAGE_MARGIN, Names.IMAGE_WIDTH));
}
