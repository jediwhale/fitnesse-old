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
    this(symbolType, content, -1, new ArrayList<>());
  }

  BranchSymbol(SymbolType symbolType, String content, int offset) {
    this(symbolType, content, offset, new ArrayList<>());
  }

  BranchSymbol(SymbolType symbolType, List<Symbol> children) {
    this(symbolType, "", -1, children);
  }

  BranchSymbol(SymbolType symbolType, String content, int offset, List<Symbol> children) {
    super(symbolType, content, offset);
    this.children = children;
  }

  @Override
  protected List<Symbol> getBranches() { return children; }

  @Override
  protected Map<String, String> getProperties() { return Collections.emptyMap(); }

  private final List<Symbol> children;
}
