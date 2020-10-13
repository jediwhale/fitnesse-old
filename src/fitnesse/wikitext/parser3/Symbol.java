package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlUtil;
import fitnesse.util.Tree;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

interface Symbol extends Tree<Symbol> {
  SymbolType getType();
  String getContent();
  void setContent(String input);
  List<Symbol> getChildren();

  static Symbol error(String message) { return new SymbolLeaf(SymbolType.ERROR, message); }

  static Symbol source(TokenType tokenType) { return new SymbolLeaf(SymbolType.SOURCE, tokenType.getMatch()); } //todo: should be from token?

  static Symbol makeList(Symbol ...children) { return make(SymbolType.LIST, children); }

  static Symbol make(SymbolType type, Symbol ...children) {
    Symbol result = new SymbolBranch(type);
    for (Symbol child: children) result.add(child);
    return result;
  }

  static Symbol make(SymbolType type, List<Symbol> children) {
    return new SymbolBranch(
      children.size() > 0 && children.get(0).getType() == SymbolType.ERROR ? SymbolType.LIST : type,
      children);
  }

  default boolean hasError() {
    return
      (getType() == SymbolType.ERROR) ||
        (hasChildren() && getChild(0).getType() == SymbolType.ERROR);
  }

  default void add(Symbol child) {
    getChildren().add(child);
  }

  default void addFirst(Symbol child) {
    getChildren().add(0, child);
  }

  default boolean hasChildren() {
    return getChildren().size() > 0;
  }

  default String getContent(int child) {
    return getChildren().get(child).getContent();
  }

  default Symbol getChild(int child) {
    return getChildren().get(child);
  }

  default Symbol getLastChild() {
    return getChildren().get(getChildren().size() - 1);
  }

  default String translateContent(TranslateSymbol<String> translator) {
    return HtmlUtil.escapeHTML(HtmlUtil.unescapeWiki(getContent())) + translateChildren(translator);
  }

  default String translateChildren(TranslateSymbol<String> translator) {
    return collectChildren(translator, new StringBuilder(), StringBuilder::append).toString();
  }

  default <T, U> U collectChildren(TranslateSymbol<T> translator, U initial, BiConsumer<U, ? super T> accumulator) {
    getChildren().stream().map(translator::translate).forEachOrdered(item -> accumulator.accept(initial, item));
    return initial;
  }

  @Override
  default Symbol getNode() { return this; }

  @Override
  default Collection<? extends Tree<Symbol>> getBranches() { return getChildren(); }
}
