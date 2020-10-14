package fitnesse.wikitext.parser3;

import java.util.HashMap;
import java.util.Map;

class TaggedSymbol extends BranchSymbol {
  TaggedSymbol(SymbolType symbolType) {
    super(symbolType);
  }

  @Override
  protected Map<String, String> getTags() { return tags; }

  private Map<String, String> tags = new HashMap<>();
}
