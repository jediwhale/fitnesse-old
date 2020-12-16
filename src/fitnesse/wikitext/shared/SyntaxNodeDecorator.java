package fitnesse.wikitext.shared;

@FunctionalInterface
public interface SyntaxNodeDecorator {
  void decorate(SyntaxNode node, ParsingPage page);
}
