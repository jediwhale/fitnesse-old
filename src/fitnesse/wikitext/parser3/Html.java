package fitnesse.wikitext.parser3;

public class Html {
  public static final String NEW_LINE = System.getProperty("line.separator");

  public static String anchor(String reference, String content) {
    return anchor(reference, "", content);
  }

  public static String anchor(String reference, String attributes, String content) {
    return "<a href=\"" + reference + "\"" + (attributes.length() > 0 ? " " : "") + attributes + ">" + content + "</a>";
  }
}