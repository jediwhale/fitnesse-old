package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.VariableStore;

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

  private final HashMap<String, String> variables = new HashMap<>();
}
