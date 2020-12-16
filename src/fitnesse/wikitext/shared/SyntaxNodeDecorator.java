package fitnesse.wikitext.shared;

import fitnesse.wikitext.ParsingPage;

@FunctionalInterface
public interface SyntaxNodeDecorator {
  void decorate(SyntaxNode node, ParsingPage page);
}
