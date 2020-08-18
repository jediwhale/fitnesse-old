package fitnesse.wikitext.parser3;

import java.util.Optional;

public interface VariableSource {
  void put(String name, String value);
  Optional<String> get(String name);
}
