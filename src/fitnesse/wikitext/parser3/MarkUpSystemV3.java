package fitnesse.wikitext.parser3;

import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiSourcePage;
import fitnesse.wikitext.MarkUpSystem;
import fitnesse.wikitext.ParsingPage;
import fitnesse.wikitext.SyntaxTree;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class MarkUpSystemV3 implements MarkUpSystem {
  @Override
  public SyntaxTree parse(ParsingPage page, String content) {
    return new SyntaxTreeV3(Parser.parse(content, ParseRules.make(page)), page);
  }

  @Override
  public String variableValueToHtml(ParsingPage page, String variableValue) {
    Symbol symbol = Parser.parse(variableValue, TokenTypes.VARIABLE_DEFINITION_TYPES, ParseRules.make(page));
    return new SyntaxTreeV3(symbol, page).translateToHtml();
  }

  @Override
  public void findWhereUsed(WikiPage page, Consumer<String> takeWhereUsed) {
    final ParsingPage parsingPage = new ParsingPage(new WikiSourcePage(page));
    Symbol symbol = Parser.parse(page.getData().getContent(), TokenTypes.REFACTORING_TYPES, ParseRules.make(parsingPage));
    SyntaxTreeV3 syntaxTree = new SyntaxTreeV3(symbol, parsingPage);
    syntaxTree.findWhereUsed(takeWhereUsed);
  }

  @Override
  public String changeReferences(WikiPage page, Function<String, Optional<String>> changeReference) {
    final ParsingPage parsingPage = new ParsingPage(new WikiSourcePage(page));
    Symbol symbol = Parser.parse(
      page.getData().getContent(),
      TokenTypes.REFACTORING_TYPES,
      ParseRules.make(parsingPage));
    SyntaxTreeV3 syntaxTree = new SyntaxTreeV3(symbol, parsingPage);
    findReferences(symbol, changeReference);
    return syntaxTree.translateToMarkUp();
  }

  private void findReferences(Symbol tree, Function<String, Optional<String>> changeReference) {
    tree.walkPreOrder(node -> {
      if (node.getType() == SymbolType.WIKI_LINK) {
        changeReference.apply(node.getContent()).ifPresent(node::setContent);
      } else if (node.getType() == SymbolType.ALIAS) {
        Symbol wikiWord = node.getChild(3).getChild(0);
        String aliasReference = wikiWord.getContent();
        if (PathParser.isWikiPath(aliasReference)) {
          changeReference.apply(aliasReference).ifPresent(wikiWord::setContent);
        }
      }
    },
    node -> node.getType() != SymbolType.ALIAS);
  }
}
