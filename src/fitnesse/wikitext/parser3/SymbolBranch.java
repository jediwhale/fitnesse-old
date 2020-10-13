package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.List;

class SymbolBranch implements Symbol {
  SymbolBranch(SymbolType symbolType) {
    this(symbolType, "");
  }

  SymbolBranch(SymbolType symbolType, String content) {
    this(symbolType, content, new ArrayList<>());
  }

  SymbolBranch(SymbolType symbolType, List<Symbol> children) {
    this(symbolType, "", children);
  }

  SymbolBranch(SymbolType symbolType, String content, List<Symbol> children) {
    this.symbolType = symbolType;
    this.content = content;
    this.children = children;
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
    return children;
  }

  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(symbolType.toString());
    if (content.length() > 0) result.append("=").append(content);
    if (children.size() > 0) {
      result.append("(");
      for (int i = 0; i < children.size(); i++) {
        if (i > 0) result.append(",");
        result.append(children.get(i).toString());
      }
      result.append(")");
    }
    return result.toString();
  }

  private String content;
  private final SymbolType symbolType;
  private final List<Symbol> children;
}
