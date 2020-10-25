package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.shared.LastModifiedHtml;
import fitnesse.wikitext.shared.ToHtml;

import java.util.EnumMap;

public class HtmlTranslator implements Translator {
  public HtmlTranslator(External external) {
    this.external = external;

    symbolTypes = new EnumMap<>(SymbolType.class);
    symbolTypes.put(SymbolType.ALIAS, (s, t) -> Alias.translate(s, this, external));
    symbolTypes.put(SymbolType.ANCHOR_NAME, Translate.with(ToHtml::anchorName).content());
    symbolTypes.put(SymbolType.ANCHOR_REFERENCE, Translate.with(ToHtml::anchorReference).content());
    symbolTypes.put(SymbolType.BOLD, Translate.with(ToHtml::pair).text("b").content());
    symbolTypes.put(SymbolType.BOLD_ITALIC, Translate.with(ToHtml::nestedPair).text("b").text("i").content());
    symbolTypes.put(SymbolType.CENTER, Translate.with(ToHtml::pair).text("center").content());
    symbolTypes.put(SymbolType.CONTENTS, (s, t) -> Contents.translate(s, external));
    symbolTypes.put(SymbolType.DEFINE, Variable::translate);
    symbolTypes.put(SymbolType.EMAIL, Translate.with(ToHtml::email).content());
    symbolTypes.put(SymbolType.ERROR, Error::translate);
    symbolTypes.put(SymbolType.SOURCE, (s, t) -> "");
    symbolTypes.put(SymbolType.HEADER, Translate.with(ToHtml::header).children());
    symbolTypes.put(SymbolType.INCLUDE, Include::translate);
    symbolTypes.put(SymbolType.ITALIC, Translate.with(ToHtml::pair).text("i").content());
    symbolTypes.put(SymbolType.LAST_MODIFIED, (s, t) -> LastModifiedHtml.write(external.getSourcePage()));
    symbolTypes.put(SymbolType.LINK, Link::translate);
    symbolTypes.put(SymbolType.WIKI_LIST, WikiList::translate);
    symbolTypes.put(SymbolType.LITERAL, Literal::translate);
    symbolTypes.put(SymbolType.LIST, Symbol::translateChildren);
    symbolTypes.put(SymbolType.NESTING, Symbol::translateChildren);
    symbolTypes.put(SymbolType.NEW_LINE, Translate.with(ToHtml::newLine));
    symbolTypes.put(SymbolType.NOTE, Translate.with(ToHtml::note).content());
    symbolTypes.put(SymbolType.PATH, Path::translate);
    symbolTypes.put(SymbolType.PREFORMAT, Translate.with(ToHtml::pair).text("pre").content());
    symbolTypes.put(SymbolType.SEE, See::translate);
    symbolTypes.put(SymbolType.STRIKE, Translate.with(ToHtml::pair).text("strike").content());
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

  private final External external;
  private final EnumMap<SymbolType, TranslateRule> symbolTypes;
  private final EnumMap<SymbolType, SymbolType> substitutes = new EnumMap<>(SymbolType.class);
}
