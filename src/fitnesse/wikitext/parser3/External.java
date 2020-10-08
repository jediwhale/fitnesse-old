package fitnesse.wikitext.parser3;

import fitnesse.wikitext.parser.Maybe;

interface External { //todo: better name
  String buildLink(String path, String description, String trailer);
  Maybe<String> findPageContent(String pageName);
  String getProperty(String name);
}
