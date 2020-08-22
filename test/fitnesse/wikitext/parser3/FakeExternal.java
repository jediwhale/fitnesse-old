package fitnesse.wikitext.parser3;

import java.util.HashMap;
import java.util.Optional;

public class FakeExternal implements External, VariableSource {
  @Override
  public String fullPath(String input) {
    return "Fake." + input;
  }

  @Override
  public boolean exists(String input) {
    return !input.contains("New");
  }

  @Override
  public void put(String name, String value) { variables.put(name, value); }

  @Override
  public Optional<String> get(String name) { return Optional.ofNullable(variables.get(name)); }

  private final HashMap<String, String> variables = new HashMap<>();
}
