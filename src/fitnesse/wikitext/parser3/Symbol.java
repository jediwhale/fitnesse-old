package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlUtil;
import fitnesse.util.Tree;
import fitnesse.wikitext.shared.PropertyStore;

import java.util.*;

abstract class Symbol extends Tree<Symbol> implements PropertyStore {

  static Symbol error(String message) { return new LeafSymbol(SymbolType.ERROR, message); }
  static Symbol text(String message) { return new LeafSymbol(SymbolType.TEXT, message); }

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

  String getContent(int child) { return getBranches().get(child).getContent(); }

  String allContents() {
    StringBuilder contents = new StringBuilder();
    walkPreOrder(branch -> contents.append(branch.getContent()));
    return contents.toString();
  }

  int getOffset() { return offset; }

  void add(Symbol child) { getBranches().add(child); }

  Symbol lastBranch() { return getBranches().get(getBranches().size() - 1); }

  boolean hasError() {
    return
      (getType() == SymbolType.ERROR) ||
        (!isLeaf() && getBranch(0).getType() == SymbolType.ERROR);
  }

  boolean isWikiReference() { return symbolType == SymbolType.WIKI_LINK && offset >= 0; }

  String translateContent(Translator translator) {
    return HtmlUtil.escapeHTML(HtmlUtil.unescapeWiki(getContent())) + translateChildren(translator);
  }

  String translateBranch(Translator translator, int branch) {
    return branch < getBranches().size() ?  translator.translate(getBranch(branch)): "";
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
      getProperties().keySet().stream().sorted()
        .forEach(key -> result.append(key).append("=").append(getProperties().get(key)).append(","));
      result.setLength(result.length() - 1);
      result.append("]");
    }
    return result.toString();
  }

  protected abstract Map<String, String> getProperties();

  protected Symbol(SymbolType symbolType) { this(symbolType, ""); }

  protected Symbol(SymbolType symbolType, String content) {
    this(symbolType, content, -1);
  }

  protected Symbol(SymbolType symbolType, String content, int offset) {
    this.symbolType = symbolType;
    this.content = content;
    this.offset = offset;
  }

  private final String content;
  private final SymbolType symbolType;
  private final int offset;
}
