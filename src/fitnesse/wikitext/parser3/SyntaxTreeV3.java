package fitnesse.wikitext.parser3;

import fitnesse.wikitext.ParsingPage;
import fitnesse.wikitext.SourcePage;
import fitnesse.wikitext.SyntaxTree;
import fitnesse.wikitext.parser.Maybe;
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
    tree.walkPreOrder(node -> result.append(node.getContent()));
    return result.toString();
  }

  @Override
  public String translateToHtml() {
    return new HtmlTranslator(new ExternalAdapter(page)).translate(tree);
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
        if (node.getChild(0).getType() == SymbolType.ALIAS) {
          takeXref.accept(node.getChild(0).getChild(3).getLastChild().getContent()); //todo: this kind of thing should be in Alias class
        } else if (node.getChild(0).getType() == SymbolType.WIKI_LINK) {
          takeXref.accept(node.getChild(0).getContent());
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
        String linkText = node.getChild(3).getChild(0).getContent(); //todo: this kind of thing should be in Alias class
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
