package fitnesse.wikitext.shared;

import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;

import java.util.Stack;

public class HeadingContentBuilder {

  private final Stack<HtmlTag> stack = new Stack<>();
  private final String listStyle;
  private final HtmlTag rootElement;
  private boolean processed;

  public HeadingContentBuilder(final String listStyle) {
    this.listStyle = listStyle;
    rootElement = new HtmlTag("div");
    rootElement.addAttribute("class", "contents");
    rootElement.add(HtmlUtil.makeBold("Contents:"));
    stack.push(rootElement);
  }

  public String html() {
    return rootElement.html();
  }

  public void htmlElements(PropertySource properties, String text) {
    processed = false;
    int level = getLevel(properties);
    addListElement(level);
    goToParent(level);
    addListItemElement(properties, text);
    if (!processed) {
      htmlElements(properties, text);
    }
  }

  private void addListElement(int level) {
    if (level > currentLevel()) {
      HtmlTag listElement = new HtmlTag("ol");
      listElement.addAttribute("style", "list-style-type: " + listStyle + ";");
      stack.peek().add(listElement);
      stack.push(listElement);
    }
  }

  private void goToParent(int level) {
    if (level < currentLevel()) {
      stack.pop();
    }
  }

  private void addListItemElement(PropertySource properties, String text) {
    if (getLevel(properties) == currentLevel()) {
      final HtmlTag listitemElement = new HtmlTag("li");
      listitemElement.addAttribute("class", "heading" + currentLevel());
      final HtmlTag anchorElement = new HtmlTag("a", text);
      properties.findProperty(Names.ID).ifPresent(id -> anchorElement.addAttribute("href", "#" + id));
      listitemElement.add(anchorElement);
      stack.peek().add(listitemElement);
      processed = true;
    }
  }

  private int currentLevel() {
    return stack.size() - 1;
  }

  private int getLevel(final PropertySource properties) {
    return Integer.parseInt(properties.findProperty(Names.LEVEL, "0"));
  }

}
