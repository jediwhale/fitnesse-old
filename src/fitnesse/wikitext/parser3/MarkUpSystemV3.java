package fitnesse.wikitext.parser3;

import fitnesse.wiki.PathParser;
import fitnesse.wikitext.MarkUpSystem;
import fitnesse.wikitext.ParsingPage;
import fitnesse.wikitext.SourcePage;
import fitnesse.wikitext.SyntaxTree;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class MarkUpSystemV3 implements MarkUpSystem {
  @Override
  public SyntaxTree parse(ParsingPage page, String content) {
    return new SyntaxTreeV3(Parser.parse(content, makeParseRules(page)), page);
  }

  @Override
  public String variableValueToHtml(ParsingPage page, String variableValue) {
    Symbol symbol = Parser.parse(variableValue, TokenTypes.VARIABLE_DEFINITION_TYPES, makeParseRules(page));
    return new SyntaxTreeV3(symbol, page).translateToHtml();
  }

  @Override
  public void findWhereUsed(SourcePage page, Consumer<String> takeWhereUsed) {
    final ParsingPage parsingPage = new ParsingPage(page);
    Symbol symbol = Parser.parse(page.getContent(), TokenTypes.REFACTORING_TYPES, makeParseRules(parsingPage));
    SyntaxTreeV3 syntaxTree = new SyntaxTreeV3(symbol, parsingPage);
    syntaxTree.findWhereUsed(takeWhereUsed);
  }

  @Override
  public String changeReferences(SourcePage page, Function<String, Optional<String>> changeReference) {
    ParsingPage parsingPage = new ParsingPage(page);
    String original = page.getContent();
    Symbol symbol = Parser.parse(original, TokenTypes.REFACTORING_TYPES, makeParseRules(parsingPage));
    Replacement replacement = new Replacement(original);
    symbol.walkPreOrder(node -> {
        if (node.getType() == SymbolType.WIKI_LINK) {
          changeReference.apply(node.getContent()).ifPresent(change -> replacement.replace(node, change));
        } else if (node.getType() == SymbolType.ALIAS) {
          Symbol wikiWord = node.getBranch(1).getBranch(0);
          String aliasReference = wikiWord.getContent();
          if (PathParser.isWikiPath(aliasReference)) {
            changeReference.apply(aliasReference).ifPresent(change -> replacement.replace(wikiWord, change));
          }
        }
      },
      node -> node.getType() != SymbolType.ALIAS);
    return replacement.makeResult();
  }

  private Map<TokenType, ParseRule> makeParseRules(ParsingPage page) {
    return ParseRules.make(page, new ExternalAdapter(page.getPage()));
  }

  private static class Replacement {
    Replacement(String original) {
      this.original = original;
    }

    void replace(Symbol before, String after) {
      changed.append(original, originalOffset, before.getOffset());
      changed.append(after);
      originalOffset = before.getOffset() + before.getContent().length();
    }

    String makeResult() {
      if (changed.length() > 0) {
        changed.append(original.substring(originalOffset));
        return changed.toString();
      }
      else {
        return original;
      }
    }

    private int originalOffset = 0;

    private final StringBuilder changed = new StringBuilder();
    private final String original;
  }
}
