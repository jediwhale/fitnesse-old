package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlUtil;
import fitnesse.util.Tree;
import fitnesse.wikitext.shared.PropertyStore;

import java.util.List;
import java.util.Map;
import java.util.Optional;

abstract class Symbol extends Tree<Symbol> implements PropertyStore {

  static Symbol error(String message) { return new LeafSymbol(SymbolType.ERROR, message); }

  static Symbol source(TokenType tokenType) { return new LeafSymbol(SymbolType.SOURCE, tokenType.getMatch()); } //todo: should be from token?

  static Symbol makeList(Symbol ...children) { return make(SymbolType.LIST, children); }

  static Symbol make(SymbolType type, Symbol ...children) {
    Symbol result = new BranchSymbol(type);
    for (Symbol child: children) result.add(child);
    return result;
  }

  static Symbol make(SymbolType type, List<Symbol> children) {
    return new BranchSymbol(
      children.size() > 0 && children.get(0).getType() == SymbolType.ERROR ? SymbolType.LIST : type,
      children);
  }

  @Override protected Symbol getNode() { return this; }

  @Override
  public Optional<String> findProperty(String key) {
    String value = getProperties().get(key);
    return value != null ? Optional.of(value) : Optional.empty();
  }

  @Override
  public boolean hasProperty(String key) { return getProperties().containsKey(key); }

  @Override
  public void putProperty(String key, String value) { getProperties().put(key, value);}

  SymbolType getType() { return symbolType; }

  String getContent() { return content; }

  void setContent(String content) { this.content = content; }

  void add(Symbol child) { getBranches().add(child); }

  void addFirst(Symbol child) { getBranches().add(0, child); }

  String getContent(int child) { return getBranches().get(child).getContent(); }

  boolean hasError() {
    return
      (getType() == SymbolType.ERROR) ||
        (!isLeaf() && getBranch(0).getType() == SymbolType.ERROR);
  }

  String translateContent(Translator translator) {
    return HtmlUtil.escapeHTML(HtmlUtil.unescapeWiki(getContent())) + translateChildren(translator);
  }

  String translateChildren(Translator translator) {
    return collectBranches(translator::translate, new StringBuilder(), StringBuilder::append).toString();
  }

  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(symbolType.toString());
    if (content.length() > 0) result.append("=").append(content);
    if (getBranches().size() > 0) {
      result.append("(");
      for (int i = 0; i < getBranches().size(); i++) {
        if (i > 0) result.append(",");
        result.append(getBranches().get(i).toString());
      }
      result.append(")");
    }
    if (getProperties().size() > 0) {
      result.append("[");
      int j = 0;
      for (String key : getProperties().keySet()) {
        if (j > 0) result.append(",");
        result.append(key).append("=").append(getProperties().get(key));
        j++;
      }
      result.append("]");
    }
    return result.toString();
  }

  protected abstract Map<String, String> getProperties();

  protected Symbol(SymbolType symbolType) { this(symbolType, ""); }

  protected Symbol(SymbolType symbolType, String content) {
    this.symbolType = symbolType;
    this.content = content;
  }

  private String content;
  private final SymbolType symbolType;
}
