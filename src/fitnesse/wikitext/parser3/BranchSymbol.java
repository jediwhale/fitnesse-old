package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class BranchSymbol extends Symbol {
  BranchSymbol(SymbolType symbolType) {
    this(symbolType, "");
  }

  BranchSymbol(SymbolType symbolType, String content) {
    this(symbolType, content, new ArrayList<>());
  }

  BranchSymbol(SymbolType symbolType, List<Symbol> children) {
    this(symbolType, "", children);
  }

  BranchSymbol(SymbolType symbolType, String content, List<Symbol> children) {
    super(symbolType, content);
    this.children = children;
  }

  @Override
  protected List<Symbol> getChildren() { return children; }

  @Override
  protected Map<String, String> getTags() { return Collections.emptyMap(); }

  private final List<Symbol> children;
}
