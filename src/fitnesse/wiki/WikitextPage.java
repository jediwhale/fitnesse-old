package fitnesse.wiki;

import fitnesse.wikitext.shared.SyntaxTree;

/**
 * This interface denotes a class that can expose parsed wiki page content.
 */
public interface WikitextPage {
  SyntaxTree getSyntaxTree();
}
