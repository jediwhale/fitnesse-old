package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.MarkUpSystem;
import fitnesse.wikitext.shared.ParsingPage;
import fitnesse.wikitext.shared.SourcePage;
import fitnesse.wikitext.shared.SyntaxTree;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class MarkUpSystemV3 implements MarkUpSystem {
  @Override
  public SyntaxTree parse(ParsingPage page, String content) {
    return new SyntaxTreeV3(Parser.parse(content, page), page);
  }

  @Override
  public String variableValueToHtml(ParsingPage page, String variableValue) {
    // this appears to be used for various properties that control processing, not for page markup
    // so a simplified set of token types are used
    Symbol symbol = Parser.parse(variableValue, VARIABLE_DEFINITION_TYPES, page);
    return new SyntaxTreeV3(symbol, page).translateToHtml();
  }

  @Override
  public void findWhereUsed(SourcePage page, Consumer<String> takeWhereUsed) {
    final ParsingPage parsingPage = new ParsingPage(page);
    Symbol symbol = Parser.parse(page.getContent(), REFACTORING_TYPES, parsingPage);
    SyntaxTreeV3 syntaxTree = new SyntaxTreeV3(symbol, parsingPage);
    syntaxTree.findWhereUsed(takeWhereUsed);
  }

  @Override
  public String changeReferences(SourcePage page, Function<String, Optional<String>> changeReference) {
    ParsingPage parsingPage = new ParsingPage(page);
    String original = page.getContent();
    Symbol symbol = Parser.parse(original, REFACTORING_TYPES, parsingPage);
    Replacement replacement = new Replacement(original);
    symbol.walkPreOrder(node -> {
        if (node.isWikiReference()) {
          changeReference.apply(node.getContent()).ifPresent(change -> replacement.replace(node, change));
        }
      });
    return replacement.makeResult();
  }

  private static final TokenTypes REFACTORING_TYPES = new TokenTypes(Arrays.asList(
    DelimiterType.ALIAS_END,
    DelimiterType.ALIAS_MIDDLE,
    DelimiterType.ALIAS_START,
    DelimiterType.COMMENT,
    DelimiterType.LINK,
    DelimiterType.LITERAL_START,
    DelimiterType.NEW_LINE,
    DelimiterType.PREFORMAT_START,

    DelimiterType.BLANK_SPACE,
    DelimiterType.BRACKET_END,
    DelimiterType.BRACKET_START
  ),
    Arrays.asList(
      KeywordType.IMAGE,
      KeywordType.PATH
  ));

  private static final TokenTypes VARIABLE_DEFINITION_TYPES = new TokenTypes(Arrays.asList(
    DelimiterType.VARIABLE_VALUE, // must be first
    DelimiterType.COMMENT,
    DelimiterType.LITERAL_START,
    DelimiterType.NEW_LINE,
    DelimiterType.PREFORMAT_START,
    DelimiterType.PREFORMAT_END,

    DelimiterType.BLANK_SPACE,
    DelimiterType.BRACE_END
  ),
    Arrays.asList(
      KeywordType.DEFINE, //todo: handle define nested too?
      KeywordType.INCLUDE
  ));


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
