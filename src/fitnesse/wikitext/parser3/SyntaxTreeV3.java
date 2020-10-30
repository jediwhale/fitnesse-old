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
    return new HtmlTranslator(new ExternalAdapter(page.getPage())).translate(tree);
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
        if (node.getBranch(0).getType() == SymbolType.ALIAS) {
          takeXref.accept(node.getBranch(0).getBranch(1).getLastBranch().getContent()); //todo: this kind of thing should be in Alias class
        } else if (node.getBranch(0).getType() == SymbolType.WIKI_LINK) {
          takeXref.accept(node.getBranch(0).getContent());
        }
      }
    });
  }

  public void findWhereUsed(Consumer<String> takeWhere) {
    tree.walkPreOrder(node -> {
      if (node.getType() == SymbolType.WIKI_LINK) {
        takeWhere.accept(node.getContent());
      }
      else if (node.getType() == SymbolType.ALIAS) {
        String linkText = node.getBranch(1).getBranch(0).getContent(); //todo: this kind of thing should be in Alias class
        if (linkText.contains("?")) {
          linkText = linkText.substring(0, linkText.indexOf('?'));
        }
        takeWhere.accept(linkText);
      }
    });
  }

  private final Symbol tree;
  private final ParsingPage page;

}
