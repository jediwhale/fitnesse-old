package fitnesse.wikitext.parser3;

import fitnesse.wikitext.SourcePage;
import fitnesse.wikitext.parser.Maybe;
import fitnesse.wikitext.parser.WikiWordBuilder;

class ExternalAdapter implements External {
  ExternalAdapter(SourcePage page) {
    this.page = page;
  }

  @Override
  public String buildLink(String path, String description, String trailer) {
    return new WikiWordBuilder(page, path, description).buildLink(trailer, path);
  }

  @Override
  public String getProperty(String name) {
    return page.getProperty(name);
  }

  @Override
  public Maybe<External> make(String pageName) {
    Maybe<SourcePage> includedPage = page.findIncludedPage(pageName);
    if (includedPage.isNothing()) return Maybe.nothingBecause(includedPage.because());
    return new Maybe<>(new ExternalAdapter(includedPage.getValue()));
  }

  @Override
  public String pageContent() {
    return page.getContent();
  }

  private final SourcePage page;
}
