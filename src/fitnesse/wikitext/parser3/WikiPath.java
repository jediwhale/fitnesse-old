package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlUtil;

public class WikiPath {

  @FunctionalInterface
  interface PagePath { String make(String path, String trailer); }

  @FunctionalInterface
  interface OtherPath { String make(String path); }

  public static Symbol parse(Parser parser) {
    Symbol result = parser.peek(0).asSymbol(SymbolType.WIKI_LINK);
    parser.advance();
    return result;
  }

  public static String makeLink(String input, PagePath makePage, OtherPath makeOther) {
    int separator = input.indexOf("?", 1); //todo: clean up
    if (separator < 0) separator = input.indexOf("#", 1);
    if (separator < 0) separator = input.length();
    String path = input.substring(0, separator);
    String trailer = input.substring(separator);
    return new Text(path).matchAll(".", WikiPath::isPageName)
      ? makePage.make(path, trailer)
      : makeOther.make(path); // path trailer is ignored when creating new page
  }

  public static boolean isWikiWordPath(String input) {
    int offset = ("<>^.".contains(input.substring(0, 1))) ? 1 : 0;
    if (offset >= input.length()) return false;
    return new Text(input, offset).matchAll(".", WikiPath::isWikiWord);
  }

  public static String translate(Symbol symbol, Translator translator) {
    final String content = symbol.translateContent(translator);
    return Link.makeWikiLink(translator.getExternal(), symbol.getContent(), "", content);
  }

  private static boolean isPageName(Text text) {
    for (int i = 0; i < text.length(); i++) {
      if (!text.at(i, WikiPath::isLetter) && !text.at(i, WikiPath::isNumber) && !text.at(i, WikiPath::isSeparator)) return false;
    }
    return true;
  }

  public static boolean isWikiWord(Text text) {
    if (!text.at(0, WikiPath::isUpperCaseLetter)) return false;
    int lastUpperCase = 0;
    for (int i = 1; i < text.length(); i++) {
      if (text.at(i, WikiPath::isUpperCaseLetter)) {
        if (lastUpperCase == i - 1) return false;
        lastUpperCase = i;
      } else if (!text.at(i, WikiPath::isLowerCaseLetterOrNumber)) return false;
    }
    return lastUpperCase > 0;
  }

  private static boolean isLowerCaseLetterOrNumber(Character candidate) {
    return isLowerCaseLetter(candidate) || isNumber(candidate);
  }

  private static boolean isLetter(Character candidate) {
    return isLowerCaseLetter(candidate) || isUpperCaseLetter(candidate);
  }

  private static boolean isNumber(Character candidate) {
    return candidate >= '0' && candidate <= '9';
  }

  private static boolean isUpperCaseLetter(Character candidate) {
    return candidate >= 'A' && candidate <= 'Z';
  }

  private static boolean isLowerCaseLetter(Character candidate) {
    return candidate >= 'a' && candidate <= 'z';
  }

  private static boolean isSeparator(Character candidate) {
    return candidate == '-' || candidate == '_';
  }
}
