package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlUtil;
import fitnesse.util.Tree;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

abstract class Symbol implements Tree<Symbol> {

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

  Optional<String> findTag(String key) {
    String value = getTags().get(key);
    return value != null ? Optional.of(value) : Optional.empty();
  }

  void putTag(String key, Optional<String> value) {
    if (value.isPresent()) putTag(key, value.get());
  }

  void putTag(String key, String value) { getTags().put(key, value);}

  boolean hasTag(String key) { return getTags().containsKey(key); }

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
    return result.toString();
  }

  protected abstract List<Symbol> getChildren();
  protected abstract Map<String, String> getTags();

  protected Symbol(SymbolType symbolType) { this(symbolType, ""); }

  protected Symbol(SymbolType symbolType, String content) {
    this.symbolType = symbolType;
    this.content = content;
  }

  private String content;
  private final SymbolType symbolType;
}
