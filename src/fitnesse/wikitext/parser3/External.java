package fitnesse.wikitext.parser3;

import fitnesse.wikitext.parser.Maybe;

interface External { //todo: better name
  String buildLink(String path, String description, String trailer);
  String getProperty(String name);
  Maybe<External> make(String pageName);
  String pageContent();
}
