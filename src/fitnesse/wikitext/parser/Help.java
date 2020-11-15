package fitnesse.wikitext.parser;

import fitnesse.wikitext.shared.Names;
import fitnesse.wikitext.shared.ToHtml;

import java.util.List;

public class Help extends SymbolType implements Rule, Translation {

  public Help() {
        super("Help");
        wikiMatcher(new Matcher().string("!help"));
        wikiRule(this);
        htmlTranslation(this);
    }

    @Override
    public Maybe<Symbol> parse(Symbol current, Parser parser) {
        List<Symbol> lookAhead = parser.peek(new SymbolType[] {SymbolType.Whitespace, SymbolType.Text});
        if (!lookAhead.isEmpty()) {
            String option = lookAhead.get(1).getContent();
            if (option.equals(Names.EDITABLE)) {
                current.putProperty(Names.EDITABLE, "");
                parser.moveNext(2);
            }
        }
        return new Maybe<>(current);
    }

    @Override
    public String toTarget(Translator translator, Symbol symbol) {
      return ToHtml.help(translator.getPage(), symbol);
    }
}
