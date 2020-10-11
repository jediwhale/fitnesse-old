package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.VariableStore;
import fitnesse.wikitext.parser.Maybe;

import java.util.HashMap;
import java.util.Optional;

public class FakeExternal implements External, VariableStore {

  @Override
  public void putVariable(String name, String value) { variables.put(name, value); }

  @Override
  public Optional<String> findVariable(String name) { return Optional.ofNullable(variables.get(name)); }

  @Override
  public String buildLink(String path, String description, String trailer) {
    return HtmlTag.name("a").attribute("href", "Fake." + path + trailer).body(description).htmlInline();
  }

  @Override
  public String getProperty(String name) {
    return name + "Value";
  }

  @Override
  public Maybe<External> make(String pageName) {
    FakeExternal result = new FakeExternal();
    result.pageName = this.pageName + "." + pageName;
    return pages.containsKey(result.pageName) ? new Maybe<>(result) : Maybe.nothingBecause("Page not found");
  }

  @Override
  public String pageContent() {
    return pages.get(pageName);
  }

  public String pageName = "root";

  public static final HashMap<String, String> pages = new HashMap<>();

  private final HashMap<String, String> variables = new HashMap<>();
}
