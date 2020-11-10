package fitnesse.wikitext.parser;

import fitnesse.wikitext.shared.HeadingContentBuilder;
import fitnesse.wikitext.shared.Names;

/**
 * Generates a ordered list of all headers from within the current wiki page.
 */
public class Headings extends SymbolType implements Rule, Translation {

  public static final Headings symbolType = new Headings();

  public Headings() {
    super("Headings");
    wikiMatcher(new Matcher().startLineOrCell().string("!headings"));
    wikiRule(this);
    htmlTranslation(this);
  }

  @Override
  public Maybe<Symbol> parse(Symbol current, Parser parser) {
    final Symbol body = parser.parseToEnd(SymbolType.Newline);

    if (body.getChildren().size() > 3
      && body.childAt(0).getType() == SymbolType.Whitespace
      && body.childAt(1).getType() == SymbolType.Text
      && body.childAt(1).getContent().equalsIgnoreCase("-" + Names.STYLE)
      && body.childAt(2).getType() == SymbolType.Whitespace
      && body.childAt(3).getType() == SymbolType.Text) {
      current.putProperty(Names.STYLE, body.childAt(3).getContent());
    }
    return new Maybe<>(current);
  }

  @Override
  public String toTarget(Translator translator, Symbol current) {
    String style = current.findProperty(Names.STYLE, Names.DEFAULT_STYLE);
    HeadingContentBuilder headingContentBuilder = new HeadingContentBuilder(Names.VALID_STYLES.contains(style) ? style : Names.DEFAULT_STYLE);
    findHeaderLines(((HtmlTranslator)translator).getSyntaxTree(), headingContentBuilder);
    return headingContentBuilder.html();
  }

  private void findHeaderLines(Symbol tree, HeadingContentBuilder builder) {
    for (final Symbol symbol : tree.getChildren()) {
      if (symbol.isType(HeaderLine.symbolType)) {
        builder.htmlElements(symbol, extractTextFromHeaderLine(symbol));
      }
    }
  }

  static String extractTextFromHeaderLine(final Symbol headerLine) {
    final StringBuilder sb = new StringBuilder();
    headerLine.walkPreOrder(node -> {
      if (node.isType(SymbolType.Text) || node.isType(Literal.symbolType) || node.isType(Whitespace)) {
        sb.append(node.getContent());
      }
    });
    return sb.toString();
  }

}
