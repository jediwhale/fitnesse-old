package fitnesse.wikitext.parser3;

import fitnesse.wikitext.ParsingPage;
import fitnesse.wikitext.SyntaxTree;

import java.util.Optional;
import java.util.function.Consumer;

public class SyntaxTreeV3 implements SyntaxTree {
  SyntaxTreeV3(Symbol tree, ParsingPage page) {
    this.tree = tree;
    this.page = page;
  }

  @Override
  public String translateToHtml() {
    return new HtmlTranslator(new ExternalAdapter(page.getPage()), tree).translate(tree);
  }

  @Override
  public Optional<String> findVariable(String name) {
    return page.findVariable(name);
  }

  @Override
  public void findPaths(Consumer<String> takePath) {
    tree.walkPostOrder(node -> {
      if (node.getType() == SymbolType.PATH) {
        Path.providePaths(node, takePath);
      }
    });
  }

  @Override
  public void findXrefs(Consumer<String> takeXref) {
    tree.walkPreOrder(node -> {
      if (node.getType() == SymbolType.SEE) { //todo: maybe just search SEE descendants for WIKI_LINK?
        if (node.getBranch(0).isWikiReference()) {
          takeXref.accept(node.getBranch(0).getContent());
        }
      }
    });
  }

  public void findWhereUsed(Consumer<String> takeWhere) {
    tree.walkPreOrder(node -> {
      if (node.isWikiReference()) {
        takeWhere.accept(node.getContent());
      }
    });
  }

  private final Symbol tree;
  private final ParsingPage page;

}
