package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.shared.LastModifiedHtml;

import java.util.EnumMap;

public class HtmlTranslator implements Translator {
  public HtmlTranslator(External external) {
    this.external = external;

    symbolTypes = new EnumMap<>(SymbolType.class);
    symbolTypes.put(SymbolType.ALIAS, (s, t) -> Alias.translate(s, this, external));
    symbolTypes.put(SymbolType.ANCHOR_NAME, AnchorName::translate);
    symbolTypes.put(SymbolType.ANCHOR_REFERENCE, AnchorReference::translate);
    symbolTypes.put(SymbolType.BOLD, Pair.translate("b"));
    symbolTypes.put(SymbolType.BOLD_ITALIC, Pair.translate("b", "i"));
    symbolTypes.put(SymbolType.CONTENTS, (s, t) -> Contents.translate(s, external));
    symbolTypes.put(SymbolType.DEFINE, Variable::translate);
    symbolTypes.put(SymbolType.ERROR, Error::translate);
    symbolTypes.put(SymbolType.SOURCE, (s, t) -> "");
    symbolTypes.put(SymbolType.HEADER, Header::translate);
    symbolTypes.put(SymbolType.INCLUDE, Include::translate);
    symbolTypes.put(SymbolType.ITALIC, Pair.translate("i"));
    symbolTypes.put(SymbolType.LAST_MODIFIED, (s, t) -> LastModifiedHtml.write(external.getSourcePage()));
    symbolTypes.put(SymbolType.LINK, Link::translate);
    symbolTypes.put(SymbolType.WIKI_LIST, WikiList::translate);
    symbolTypes.put(SymbolType.LITERAL, Literal::translate);
    symbolTypes.put(SymbolType.LIST, Symbol::translateChildren);
    symbolTypes.put(SymbolType.NESTING, Symbol::translateChildren);
    symbolTypes.put(SymbolType.NEW_LINE, inLine("br"));
    symbolTypes.put(SymbolType.PATH, Path::translate);
    symbolTypes.put(SymbolType.SEE, See::translate);
    symbolTypes.put(SymbolType.STRIKE, Pair.translate("strike"));
    symbolTypes.put(SymbolType.STYLE, Style::translate);
    symbolTypes.put(SymbolType.TABLE, Table::translate);
    symbolTypes.put(SymbolType.TEXT, Symbol::translateContent);
    symbolTypes.put(SymbolType.WIKI_LINK, (s, t) -> WikiPath.translate(s, t, external));
  }

  public HtmlTranslator(HtmlTranslator original) {
    external = original.external;
    symbolTypes = original.symbolTypes;
  }

  public Translator substitute(SymbolType originalType, SymbolType substituteType) {
    HtmlTranslator substitute = new HtmlTranslator(this);
    substitute.substitutes.put(originalType,  substituteType);
    return substitute;
  }

  @Override
  public String translate(Symbol symbol) {
    SymbolType type = symbol.getType();
    if (substitutes.containsKey(type)) type = substitutes.get(type);
    return symbolTypes.get(type).translate(symbol, this);
  }

  private static TranslateRule inLine(String tag) {
    return (symbol, translator) -> HtmlTag.name(tag).htmlInline();
  }

  private final External external;
  private final EnumMap<SymbolType, TranslateRule> symbolTypes;
  private final EnumMap<SymbolType, SymbolType> substitutes = new EnumMap<>(SymbolType.class);
}
