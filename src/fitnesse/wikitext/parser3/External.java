package fitnesse.wikitext.parser3;

import fitnesse.wikitext.parser.Maybe;
import fitnesse.wikitext.shared.SourcePage;

interface External { //todo: better name
  SourcePage getSourcePage();
  Maybe<External> make(String pageName);
}
