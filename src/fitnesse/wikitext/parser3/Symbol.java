package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlUtil;
import fitnesse.util.Tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

class Symbol implements Tree<Symbol> {
  static Symbol error(String message) { return new Symbol(SymbolType.ERROR, message); }

  static Symbol source(TokenType tokenType) { return new Symbol(SymbolType.SOURCE, tokenType.getMatch()); }

  static Symbol makeList(Symbol ...children) { return make(SymbolType.LIST, children); }

  static Symbol make(SymbolType type, Symbol ...children) {
    Symbol result = new Symbol(type);
    for (Symbol child: children) result.add(child);
    return result;
  }

  static Symbol make(SymbolType type, List<Symbol> children) {
    return new Symbol(
      children.size() > 0 && children.get(0).getType() == SymbolType.ERROR ? SymbolType.LIST : type,
      children);
  }

  Symbol(SymbolType type, String content) {
    this.type = type;
    this.content = content;
    children = new ArrayList<>();
  }

  Symbol(SymbolType type, List<Symbol> children) {
    this.type = type;
    this.content = "";
    this.children = children;
  }

  Symbol(SymbolType type) {
    this(type, "");
  }

  boolean hasError() {
    return
      (type == SymbolType.ERROR) ||
        (hasChildren() && getChild(0).getType() == SymbolType.ERROR);
  }

  void add(Symbol child) {
    children.add(child);
  }

  void addFirst(Symbol child) {
    children.add(0, child);
  }

  boolean hasChildren() {
    return children.size() > 0;
  }

  String getContent(int child) {
    return children.get(child).getContent();
  }

  Symbol getChild(int child) {
    return children.get(child);
  }

  Symbol getLastChild() {
    return children.get(children.size() - 1);
  }

  String getContent() {
    return content;
  }

  void setContent(String input) {
    content = input;
  }

  SymbolType getType() {
    return type;
  }

  String translateContent(TranslateSymbol<String> translator) {
    return HtmlUtil.escapeHTML(HtmlUtil.unescapeWiki(content)) + translateChildren(translator);
  }

  String translateChildren(TranslateSymbol<String> translator) {
    return collectChildren(translator, new StringBuilder(), StringBuilder::append).toString();
  }

  <T, U> U collectChildren(TranslateSymbol<T> translator, U initial, BiConsumer<U, ? super T> accumulator) {
    children.stream().map(translator::translate).forEachOrdered(item -> accumulator.accept(initial, item));
    return initial;
  }

  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(type.toString());
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
  private final SymbolType type;
  private final List<Symbol> children;

  @Override
  public Symbol getNode() { return this; }

  @Override
  public Collection<? extends Tree<Symbol>> getBranches() { return children; }
}
