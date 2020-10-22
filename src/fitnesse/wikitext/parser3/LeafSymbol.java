package fitnesse.wikitext.parser3;

import java.util.Collections;
import java.util.List;
import java.util.Map;

class LeafSymbol extends Symbol {
  LeafSymbol(SymbolType symbolType, String content) {
    super(symbolType, content);
  }

  @Override
  protected List<Symbol> getBranches() { return Collections.emptyList(); }

  @Override
  protected Map<String, String> getProperties() { return Collections.emptyMap(); }
}
