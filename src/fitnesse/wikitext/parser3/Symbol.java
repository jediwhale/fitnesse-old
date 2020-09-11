package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlUtil;
import fitnesse.util.Tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

public class Symbol implements Tree<Symbol> {
  public static Symbol error(String message) { return new Symbol(SymbolType.ERROR, message); }

  public static Symbol makeList(Symbol ...children) { return make(SymbolType.LIST, children); }

  public static Symbol make(SymbolType type, Symbol ...children) {
    Symbol result = new Symbol(type);
    for (Symbol child: children) result.add(child);
    return result;
  }

  public Symbol(SymbolType type, String content) {
    this.type = type;
    this.content = content;
    children = new ArrayList<>();
  }

  public Symbol(SymbolType type) {
    this(type, "");
  }

  public void addError(String error) {
    add(Symbol.error(error));
  }

  public void addErrorFirst(String error) {
    addFirst(Symbol.error(error));
  }

  public boolean hasError() {
    return
      (type == SymbolType.ERROR) ||
        (hasChild(0) && getChild(0).getType() == SymbolType.ERROR);
  }

  public Symbol asType(SymbolType type) {
    if (this.type == type || hasError()) return this;
    Symbol result = new Symbol(type);
    for (Symbol child: children) result.add(child);
    return result;
  }

  public void add(Symbol child) {
    children.add(child);
  }

  public void addFirst(Symbol child) {
    children.add(0, child);
  }

  public boolean hasChild(int child) {
    return child < children.size();
  }

  public String getContent(int child) {
    return children.get(child).getContent();
  }

  public Symbol getChild(int child) {
    return children.get(child);
  }

  public String getContent() {
    return content;
  }

  public void setContent(String input) {
    content = input;
  }

  public SymbolType getType() {
    return type;
  }

  public String translateContent(TranslateSymbol<String> translator) {
    return HtmlUtil.escapeHTML(content) + translateChildren(translator);
  }

  public String translateChildren(TranslateSymbol<String> translator) {
    return collectChildren(translator, new StringBuilder(), StringBuilder::append).toString();
  }

  public <T, U> U collectChildren(TranslateSymbol<T> translator, U initial, BiConsumer<U, ? super T> accumulator) {
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
  public Collection<? extends Tree<Symbol>> getChildren() { return children; }
}
