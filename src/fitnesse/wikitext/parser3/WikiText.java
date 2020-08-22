package fitnesse.wikitext.parser3;

import fitnesse.wikitext.parser.ParsingPage;
import fitnesse.wikitext.parser.SourcePage;
import fitnesse.wikitext.parser.WikiWordBuilder;

import java.util.Optional;

public class WikiText {

  public WikiText(fitnesse.wikitext.parser.VariableSource variableSource, ParsingPage parsingPage) {
    variables = new VariableSourceAdapter(variableSource, parsingPage);
    external = new ExternalAdapter(parsingPage.getNamedPage());
  }

  public String toHtml(String input) {
    return new HtmlTranslator(external).translate(Parser.parse(input, variables));
  }

  private final External external;
  private final VariableSource variables;

  private static class ExternalAdapter implements External {
    public ExternalAdapter(SourcePage sourcePage) {
      this.sourcePage = sourcePage;
    }

    @Override
    public String fullPath(String input) {
      String wikiWordPath = WikiWordBuilder.makePath(sourcePage, input);
      return sourcePage.makeFullPathOfTarget(wikiWordPath);
    }

    @Override
    public boolean exists(String input) {
      return !sourcePage.targetExists(input);
    }

    private final SourcePage sourcePage;
  }

  private static class VariableSourceAdapter implements VariableSource {
    public VariableSourceAdapter(fitnesse.wikitext.parser.VariableSource variableSource, ParsingPage parsingPage) {
      this.variableSource = variableSource;
      this.parsingPage = parsingPage;
    }

    @Override
    public void put(String name, String value) {
      parsingPage.putVariable(name, value);
    }

    @Override
    public Optional<String> get(String name) {
      fitnesse.wikitext.parser.Maybe<String> result = variableSource.findVariable(name);
      return result.isNothing() ? Optional.empty() : Optional.of(result.getValue());
    }

    private final fitnesse.wikitext.parser.VariableSource variableSource;
    private final ParsingPage parsingPage;
  }
}
