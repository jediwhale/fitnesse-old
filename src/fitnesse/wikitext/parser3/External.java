package fitnesse.wikitext.parser3;

import fitnesse.wikitext.SourcePage;
import fitnesse.wikitext.parser.Maybe;

interface External { //todo: better name
  SourcePage getSourcePage();
  Maybe<External> make(String pageName);
}
