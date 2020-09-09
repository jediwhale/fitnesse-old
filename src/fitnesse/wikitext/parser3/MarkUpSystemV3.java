package fitnesse.wikitext.parser3;

import fitnesse.wiki.WikiPage;
import fitnesse.wikitext.MarkUpSystem;
import fitnesse.wikitext.SyntaxTree;
import fitnesse.wikitext.parser.ParsingPage;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class MarkUpSystemV3 implements MarkUpSystem {
  @Override
  public SyntaxTree parse(ParsingPage page, String content) {
    return new SyntaxTreeV3(Parser.parse(content, new VariableSourceAdapter(page)), page);
  }

  @Override
  public String variableValueToHtml(ParsingPage page, String variableValue) {
    Symbol symbol = Parser.parse(variableValue, new VariableSourceAdapter(page), new Scanner(TokenTypes.VARIABLE_DEFINITION_TYPES));
    return new SyntaxTreeV3(symbol, page).translateToHtml();
  }

  @Override
  public void findWhereUsed(WikiPage page, Consumer<String> takeWhereUsed) {

  }

  @Override
  public String changeReferences(WikiPage page, Function<String, Optional<String>> changeReference) {
    return "*not implemented*";
  }

  private static class VariableSourceAdapter implements VariableSource {
    public VariableSourceAdapter(ParsingPage parsingPage) {
      this.parsingPage = parsingPage;
    }

    @Override
    public void put(String name, String value) {
      parsingPage.putVariable(name, value);
    }

    @Override
    public Optional<String> get(String name) {
      return parsingPage.findVariable(name);
    }

    private final ParsingPage parsingPage;

  }
}
