package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;

import java.util.HashMap;
import java.util.Optional;

public class FakeExternal implements External, VariableSource {

  @Override
  public void put(String name, String value) { variables.put(name, value); }

  @Override
  public Optional<String> get(String name) { return Optional.ofNullable(variables.get(name)); }

  @Override
  public String buildLink(String path, String description, String trailer) {
    return HtmlTag.name("a").attribute("href", "Fake." + path + trailer).body(description).htmlInline();
  }

  private final HashMap<String, String> variables = new HashMap<>();
}
