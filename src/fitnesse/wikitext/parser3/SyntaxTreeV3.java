package fitnesse.wikitext.parser3;

import fitnesse.util.TreeWalker;
import fitnesse.wikitext.ParsingPage;
import fitnesse.wikitext.SourcePage;
import fitnesse.wikitext.SyntaxTree;
import fitnesse.wikitext.parser.WikiWordBuilder;

import java.util.Optional;
import java.util.function.Consumer;

public class SyntaxTreeV3 implements SyntaxTree {
  SyntaxTreeV3(Symbol tree, ParsingPage page) {
    this.tree = tree;
    this.page = page;
  }

  public String translateToMarkUp() { //todo: if errors in tree??
    StringBuilder result = new StringBuilder();
    tree.walkPreOrder(new TreeWalker<Symbol>() {
      @Override
      public boolean visit(Symbol node) {
        result.append(node.getContent());
        return true;
      }

      @Override
      public boolean visitBranches(Symbol node) { return true; }
    });
    return result.toString();
  }

  @Override
  public String translateToHtml() {
    return new HtmlTranslator(new ExternalAdapter(page.getPage())).translate(tree);
  }

  @Override
  public Optional<String> findVariable(String name) {
    return page.findVariable(name);
  }

  @Override
  public void findPaths(Consumer<String> takePath) {

  }

  @Override
  public void findXrefs(Consumer<String> takeXref) {
    tree.walkPreOrder(new TreeWalker<Symbol>() {
      @Override
      public boolean visit(Symbol node) {
        if (node.getType() == SymbolType.SEE) { //todo: maybe just search SEE descendants for WIKI_LINK?
          if (node.getChild(0).getType() == SymbolType.ALIAS) {
            takeXref.accept(node.getChild(0).getChild(3).getLastChild().getContent()); //todo: this kind of thing should be in Alias class
          } else if (node.getChild(0).getType() == SymbolType.WIKI_LINK) {
            takeXref.accept(node.getChild(0).getContent());
          }
        }
        return true;
      }

      @Override
      public boolean visitBranches(Symbol node) { return true; }
    });
  }

  private final Symbol tree;
  private final ParsingPage page;

  private static class ExternalAdapter implements External {
    public ExternalAdapter(SourcePage page) {
      this.page = page;
    }

    @Override
    public String buildLink(String path, String description, String trailer) {
      return new WikiWordBuilder(page, path, description).buildLink(trailer, path);
    }

    private final SourcePage page;
  }
}
