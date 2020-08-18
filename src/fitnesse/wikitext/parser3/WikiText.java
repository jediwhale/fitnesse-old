package fitnesse.wikitext.parser3;

import fitnesse.wikitext.parser.ParsingPage;

import java.util.Optional;

public class WikiText {

  public WikiText(fitnesse.wikitext.parser.VariableSource variableSource, ParsingPage parsingPage) {
    variables = new VariableSourceAdapter(variableSource, parsingPage);
  }
  public String toHtml(String input) {
    return new HtmlTranslator(external).translate(Parser.parse(input, variables));
  }

  External external = new FakeExternal();
  VariableSource variables;

  private  static class FakeExternal implements External {
    @Override
    public String translateLink(String path, LinkFormatter existingPage, LinkFormatter newPage) {
      return path.contains("New") ? newPage.formatLink("Fake." + path) : existingPage.formatLink("Fake." + path);
    }
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
