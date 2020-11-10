package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class HeadingsTest {
  @Test public void scans() {
    assertScans("Headings=!headings", "!headings");
  }

  @Test public void parses() {
    assertParses("HEADINGS", "!headings");
    assertParses("HEADINGS[STYLE=decimal]", "!headings -STYLE decimal");
  }

  @Test public void translates() {
    assertTranslates(
      "<div class=\"contents\">\n" +
        "\t<b>Contents:</b>\n" +
        "\t<ol style=\"list-style-type: decimal;\">\n" +
        "\t\t<li class=\"heading1\">\n" +
        "\t\t\t<a href=\"#0\">one</a>\n" +
        "\t\t</li>\n" +
        "\t\t<ol style=\"list-style-type: decimal;\">\n" +
        "\t\t\t<li class=\"heading2\">\n" +
        "\t\t\t\t<a href=\"#1\">two</a>\n" +
        "\t\t\t</li>\n" +
        "\t\t</ol>\n" +
        "\t</ol>\n" +
        "</div>\n" +
        "<br/><h1 id=\"0\">one</h1>\n" +
        "<h2 id=\"1\">two</h2>\n",
      "!headings\n!1 one\n!2 two\n");
  }
}
