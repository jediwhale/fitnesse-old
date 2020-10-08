package fitnesse.wikitext.parser3;

import fitnesse.wikitext.ParsingPage;
import fitnesse.wikitext.SourcePage;
import fitnesse.wikitext.parser.Maybe;
import fitnesse.wikitext.parser.WikiWordBuilder;

class ExternalAdapter implements External {
  ExternalAdapter(ParsingPage page) {
    this.page = page.getPage();
  }

  @Override
  public String buildLink(String path, String description, String trailer) {
    return new WikiWordBuilder(page, path, description).buildLink(trailer, path);
  }

  @Override
  public Maybe<String> findPageContent(String pageName) {
    Maybe<SourcePage> includedPage = page.findIncludedPage(pageName);
    if (includedPage.isNothing()) return Maybe.nothingBecause(includedPage.because());
    return new Maybe<>(includedPage.getValue().getContent());
  }

  @Override
  public String getProperty(String name) {
    return page.getProperty(name);
  }

  private final SourcePage page;
}
