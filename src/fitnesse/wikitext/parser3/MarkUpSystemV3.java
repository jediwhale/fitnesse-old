package fitnesse.wikitext.parser3;

import fitnesse.util.Tree;
import fitnesse.util.TreeWalker;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiSourcePage;
import fitnesse.wikitext.MarkUpSystem;
import fitnesse.wikitext.SyntaxTree;
import fitnesse.wikitext.parser.ParsingPage;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class MarkUpSystemV3 implements MarkUpSystem {
  @Override
  public SyntaxTree parse(ParsingPage page, String content) {
    return new SyntaxTreeV3(Parser.parse(content, page), page);
  }

  @Override
  public String variableValueToHtml(ParsingPage page, String variableValue) {
    Symbol symbol = Parser.parse(variableValue, page, new Scanner(TokenTypes.VARIABLE_DEFINITION_TYPES));
    return new SyntaxTreeV3(symbol, page).translateToHtml();
  }

  @Override
  public void findWhereUsed(WikiPage page, Consumer<String> takeWhereUsed) {

  }

  @Override
  public String changeReferences(WikiPage page, Function<String, Optional<String>> changeReference) {
    final ParsingPage parsingPage = new ParsingPage(new WikiSourcePage(page));
    Symbol symbol = Parser.parse(
      page.getData().getContent(),
      parsingPage,
      new Scanner(TokenTypes.REFACTORING_TYPES));
    SyntaxTreeV3 syntaxTree = new SyntaxTreeV3(symbol, parsingPage);
    findReferences(symbol, changeReference);
    return syntaxTree.translateToMarkUp();
  }

  private void findReferences(Symbol tree, Function<String, Optional<String>> changeReference) {
    tree.walkPreOrder(new TreeWalker<Symbol>() {
      @Override
      public boolean visit(Tree<Symbol> tree) {
        Symbol node = tree.getNode();
        if (node.getType() == SymbolType.WIKI_LINK) {
          changeReference.apply(node.getContent()).ifPresent(node::setContent);
        } else if (node.getType() == SymbolType.ALIAS) {
          Symbol wikiWord = node.getChild(1).getChild(0);
          String aliasReference = wikiWord.getContent();
          if (PathParser.isWikiPath(aliasReference)) {
            changeReference.apply(aliasReference).ifPresent(wikiWord::setContent);
          }
        }
        return true;
      }

      @Override
      public boolean visitChildren(Symbol node) {
        return node.getType() != SymbolType.ALIAS;
      }
    });
  }
}
