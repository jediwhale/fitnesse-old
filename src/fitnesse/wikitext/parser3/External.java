package fitnesse.wikitext.parser3;

import fitnesse.wikitext.SourcePage;
import fitnesse.wikitext.parser.Maybe;

interface External { //todo: better name
  SourcePage getSourcePage(); // todo: should we still have other methods if we're exposing this?
  String buildLink(String path, String description, String trailer);
  String getProperty(String name);
  Maybe<External> make(String pageName);
  String pageContent();
}
