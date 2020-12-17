package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.LastModifiedHtml;
import fitnesse.wikitext.shared.MarkUpConfig;
import fitnesse.wikitext.shared.ParsingPage;
import fitnesse.wikitext.shared.ToHtml;

import java.util.EnumMap;

public class HtmlTranslator implements Translator {

  public HtmlTranslator(External external, Symbol syntaxTree, ParsingPage page) {
    //todo: external and page are overlapping members...
    this.page = page;
    symbolTypes = new EnumMap<>(SymbolType.class);
    symbolTypes.put(SymbolType.ANCHOR_NAME, Translate.with(ToHtml::anchorName).content());
    symbolTypes.put(SymbolType.ANCHOR_REFERENCE, Translate.with(ToHtml::anchorReference).content());
    symbolTypes.put(SymbolType.BOLD, Translate.with(ToHtml::pair).text("b").content());
    symbolTypes.put(SymbolType.BOLD_ITALIC, Translate.with(ToHtml::nestedPair).text("b").text("i").content());
    symbolTypes.put(SymbolType.CENTER, Translate.with(ToHtml::pair).text("center").content());
    symbolTypes.put(SymbolType.COLLAPSIBLE, Translate.with(ToHtml::collapsible).branch(0).branch(1));
    symbolTypes.put(SymbolType.CONTENTS, (s, t) -> Contents.translate(s, external));
    symbolTypes.put(SymbolType.DEFINE, Define::translate);
    symbolTypes.put(SymbolType.EMAIL, Translate.with(ToHtml::email).content());
    symbolTypes.put(SymbolType.ERROR, Translate.with(ToHtml::error).content());
    symbolTypes.put(SymbolType.EXPRESSION, Translate.with(ToHtml::expression).content());
    symbolTypes.put(SymbolType.HASH_TABLE, HashTable::translate);
    symbolTypes.put(SymbolType.HEADER, Translate.with(ToHtml::header).children());
    symbolTypes.put(SymbolType.HEADINGS, (symbol, t) -> Headings.translate(symbol, syntaxTree));
    symbolTypes.put(SymbolType.HELP, (s, t) -> ToHtml.help(external.getSourcePage(), s));
    symbolTypes.put(SymbolType.IMAGE, Translate.with(ToHtml::image).content());
    symbolTypes.put(SymbolType.INCLUDE, Include::translate);
    symbolTypes.put(SymbolType.ITALIC, Translate.with(ToHtml::pair).text("i").content());
    symbolTypes.put(SymbolType.LAST_MODIFIED, (s, t) -> LastModifiedHtml.write(external.getSourcePage()));
    symbolTypes.put(SymbolType.LINK, Translate.with(ToHtml::link).leaf().branch(0).branch(1));
    symbolTypes.put(SymbolType.WIKI_LIST, WikiList::translate);
    symbolTypes.put(SymbolType.LITERAL, Literal::translate);
    symbolTypes.put(SymbolType.LIST, Symbol::translateChildren);
    symbolTypes.put(SymbolType.META, Translate.with(ToHtml::meta).content());
    symbolTypes.put(SymbolType.NESTING, Symbol::translateChildren);
    symbolTypes.put(SymbolType.NEW_LINE, Translate.with(ToHtml::newLine));
    symbolTypes.put(SymbolType.NOTE, Translate.with(ToHtml::note).content());
    symbolTypes.put(SymbolType.PATH, Translate.with(ToHtml::path).content());
    symbolTypes.put(SymbolType.PREFORMAT, Translate.with(ToHtml::pair).text("pre").content());
    symbolTypes.put(SymbolType.SEE, See::translate);
    symbolTypes.put(SymbolType.STRIKE, Translate.with(ToHtml::pair).text("strike").content());
    symbolTypes.put(SymbolType.STYLE, Style::translate);
    symbolTypes.put(SymbolType.TABLE, Table::translate);
    symbolTypes.put(SymbolType.TEXT, Symbol::translateContent);
    symbolTypes.put(SymbolType.WIKI_LINK, Translate.with(s -> WikiPath.toHtml(s, external)).leaf().branch(0).branch(1).branch(2));
  }

  @Override
  public TranslateRule findRule(SymbolType symbolType) {
    return symbolTypes.get(symbolType);
  }

  @Override
  public void decorate(Symbol symbol) {
    MarkUpConfig.decorate(symbol, page);
  }

  private final ParsingPage page;
  private final EnumMap<SymbolType, TranslateRule> symbolTypes;
}
