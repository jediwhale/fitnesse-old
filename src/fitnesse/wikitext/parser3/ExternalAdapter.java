package fitnesse.wikitext.parser3;

import fitnesse.wikitext.parser.Maybe;
import fitnesse.wikitext.shared.SourcePage;

class ExternalAdapter implements External {
  ExternalAdapter(SourcePage page) {
    this.page = page;
  }

  @Override
  public SourcePage getSourcePage() {
    return page;
  }

  @Override
  public Maybe<External> make(String pageName) {
    Maybe<SourcePage> includedPage = page.findIncludedPage(pageName);
    if (includedPage.isNothing()) return Maybe.nothingBecause(includedPage.because());
    return new Maybe<>(new ExternalAdapter(includedPage.getValue()));
  }

  private final SourcePage page;
}
