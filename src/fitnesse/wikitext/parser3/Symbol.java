package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlUtil;
import fitnesse.util.Tree;
import fitnesse.wikitext.shared.PropertyStore;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

abstract class Symbol implements Tree<Symbol>, PropertyStore {

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

  @Override
  public Symbol getNode() { return this; }

  @Override
  public Collection<? extends Tree<Symbol>> getBranches() { return getChildren(); }

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

  void add(Symbol child) { getChildren().add(child); }

  void addFirst(Symbol child) { getChildren().add(0, child); }

  boolean hasChildren() { return getChildren().size() > 0; }

  String getContent(int child) { return getChildren().get(child).getContent(); }

  Symbol getChild(int child) { return getChildren().get(child); }

  Symbol getLastChild() { return getChildren().get(getChildren().size() - 1); }

  boolean hasError() {
    return
      (getType() == SymbolType.ERROR) ||
        (hasChildren() && getChild(0).getType() == SymbolType.ERROR);
  }

  String translateContent(TranslateSymbol<String> translator) {
    return HtmlUtil.escapeHTML(HtmlUtil.unescapeWiki(getContent())) + translateChildren(translator);
  }

  String translateChildren(TranslateSymbol<String> translator) {
    return collectChildren(translator, new StringBuilder(), StringBuilder::append).toString();
  }

  <T, U> U collectChildren(TranslateSymbol<T> translator, U initial, BiConsumer<U, ? super T> accumulator) {
    getChildren().stream().map(translator::translate).forEachOrdered(item -> accumulator.accept(initial, item));
    return initial;
  }

  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(symbolType.toString());
    if (content.length() > 0) result.append("=").append(content);
    if (getChildren().size() > 0) {
      result.append("(");
      for (int i = 0; i < getChildren().size(); i++) {
        if (i > 0) result.append(",");
        result.append(getChildren().get(i).toString());
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

  protected abstract List<Symbol> getChildren();
  protected abstract Map<String, String> getProperties();

  protected Symbol(SymbolType symbolType) { this(symbolType, ""); }

  protected Symbol(SymbolType symbolType, String content) {
    this.symbolType = symbolType;
    this.content = content;
  }

  private String content;
  private final SymbolType symbolType;
}
