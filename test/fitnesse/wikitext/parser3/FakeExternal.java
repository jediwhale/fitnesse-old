package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.SourcePage;
import fitnesse.wikitext.VariableStore;
import fitnesse.wikitext.parser.Maybe;

import java.util.HashMap;
import java.util.Optional;

public class FakeExternal implements External, VariableStore {
  public FakeExternal(SourcePage sourcePage) {
    this.sourcePage = sourcePage;
  }

  @Override
  public void putVariable(String name, String value) { variables.put(name, value); }

  @Override
  public Optional<String> findVariable(String name) { return Optional.ofNullable(variables.get(name)); }

  @Override
  public SourcePage getSourcePage() {
    return sourcePage;
  }

  @Override
  public Maybe<External> make(String pageName) {
    FakeSourcePage page = new FakeSourcePage();
    FakeExternal result = new FakeExternal(page);
    result.pageName = this.pageName + "." + pageName;
    page.content = pages.getOrDefault(result.pageName, result.pageName + " content");
    return pages.containsKey(result.pageName) ? new Maybe<>(result) : Maybe.nothingBecause("Page not found");
  }

  @Override
  public int nextId() {
    return id++;
  }

  public String pageName = "root";
  public SourcePage sourcePage;

  public static final HashMap<String, String> pages = new HashMap<>();
  public int id;
  private final HashMap<String, String> variables = new HashMap<>();
}
