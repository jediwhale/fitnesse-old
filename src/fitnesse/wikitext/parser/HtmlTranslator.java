package fitnesse.wikitext.parser;

import fitnesse.wikitext.ParsingPage;
import fitnesse.wikitext.SourcePage;
import fitnesse.wikitext.shared.MarkUpConfig;

public class HtmlTranslator extends Translator {
  public HtmlTranslator(SourcePage currentPage, SyntaxTreeV2 syntaxTree) {
    super(currentPage);
    this.syntaxTree = syntaxTree;
  }

  public ParsingPage getParsingPage() { return syntaxTree.getParsingPage(); }
  public Symbol getSyntaxTree() { return syntaxTree.getSyntaxTree(); }

  @Override
  protected Translation getTranslation(SymbolType symbolType) {
    return symbolType.getHtmlTranslation();
  }

  @Override
  protected Translation getTranslation(Symbol symbol) {
    //apply decorators
    MarkUpConfig.decorate(symbol, getParsingPage());

    // apply deprecated decorators
    symbol.getType().applyParsedSymbolDecorations(symbol, getParsingPage());

    return super.getTranslation(symbol);
  }

  private final SyntaxTreeV2 syntaxTree;
}
