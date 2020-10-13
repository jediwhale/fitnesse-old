package fitnesse.wikitext.parser3;

import java.util.Collections;
import java.util.List;

class SymbolLeaf implements Symbol {
  SymbolLeaf(SymbolType symbolType) {
    this(symbolType, "");
  }

  SymbolLeaf(SymbolType symbolType, String content) {
    this.symbolType = symbolType;
    this.content = content;
  }

  @Override
  public SymbolType getType() {
    return symbolType;
  }

  @Override
  public String getContent() {
    return content;
  }

  @Override
  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public List<Symbol> getChildren() {
    return Collections.emptyList();
  }

  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(symbolType.toString());
    if (content.length() > 0) result.append("=").append(content);
    return result.toString();
  }

  private String content;
  private final SymbolType symbolType;
}
