package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlUtil;
import fitnesse.wikitext.parser.WikiWordBuilder;
import fitnesse.wikitext.shared.PageSources;

class WikiPath { //todo: dry with V2, better name

  static int pagePathLength(String input) {
    int length = input.indexOf("?", 1); //todo: clean up
    if (length < 0) length = input.indexOf("#", 1);
    if (length < 0) length = input.length();
    String path = input.substring(0, length);
    return isPagePath(path) ? length : 0;
  }

  static String toHtml(String[] strings, PageSources sources) {
    String path = !strings[1].equals("") ? strings[1] : strings[0];
    String description = !strings[2].equals("") ? strings[2] : HtmlUtil.escapeHTML(strings[0]);
    return new WikiWordBuilder(sources.getPage(), path, description).buildLink(strings[3], path);
  }

  private static boolean isPagePath(String path) {
    return new Text(path, nameOffset(path)).matchAll(".", WikiPath::isPageName);
  }

  private static int nameOffset(String input) {
    return ("<>^.".contains(input.substring(0, 1))) ? 1 : 0;
  }

  private static boolean isPageName(Text text) {
    for (int i = 0; i < text.length(); i++) {
      if (!text.at(i, WikiPath::isLetter) && !text.at(i, WikiPath::isNumber) && !text.at(i, WikiPath::isSeparator)) return false;
    }
    return true;
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
