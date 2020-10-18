package fitnesse.wikitext.parser3;

import java.util.HashMap;
import java.util.Map;

class TaggedSymbol extends BranchSymbol {
  TaggedSymbol(SymbolType symbolType) {
    super(symbolType);
  }

  @Override
  protected Map<String, String> getProperties() { return properties; }

  private final Map<String, String> properties = new HashMap<>();
}
