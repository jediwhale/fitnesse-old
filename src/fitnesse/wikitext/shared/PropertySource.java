package fitnesse.wikitext.shared;

import java.util.Optional;
import java.util.function.BiConsumer;

public interface PropertySource {
  Optional<String> findProperty(String key);
  boolean hasProperty(String key);

  default String findProperty(String key, String defaultValue) {
    return findProperty(key).orElse(defaultValue);
  }

  default void ifPresent(String key, BiConsumer<String, String> action) {
    findProperty(key).ifPresent(value -> action.accept(key, value));
  }
}
