package fitnesse.wikitext.shared;

import fitnesse.wikitext.VariableSource;

public interface PropertyStore extends PropertySource {

  void putProperty(String key, String value);

  default void copyVariables(String[] names, VariableSource source) {
    for (String name: names) {
      source.findVariable(name).ifPresent(value -> putProperty(name, value));
    }
  }

  default void appendProperty(String key, String value) {
    if (hasProperty(key)) {
      final String existingValue = findProperty(Names.CLASS, "");
      if (!alreadyContainsValue(existingValue, value)) {
        putProperty(key, existingValue + " " + value);
      }
    } else {
      putProperty(key, value);
    }
  }

  static boolean alreadyContainsValue(String existingValue, String valueToAdd) {
    return wrapInSpace(existingValue).contains(wrapInSpace(valueToAdd));
  }

  static String wrapInSpace(String value) {
    return " " + value + " ";
  }
}
