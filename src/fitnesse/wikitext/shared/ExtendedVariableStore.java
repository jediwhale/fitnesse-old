package fitnesse.wikitext.shared;

import java.util.Optional;

public class ExtendedVariableStore implements VariableStore {

  public ExtendedVariableStore(VariableStore base, VariableSource extension) {
    this.base = base;
    this.extension = extension;
  }

  @Override
  public void putVariable(String name, String value) {
    base.putVariable(name, value);
  }

  @Override
  public int nextId() {
    return base.nextId();
  }

  @Override
  public Optional<String> findVariable(String name) {
    Optional<String> value = extension.findVariable(name);
    return value.isPresent() ?  value : base.findVariable(name);
  }

  private final VariableStore base;
  private final VariableSource extension;
}
