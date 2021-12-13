package fitnesse.fixtures;

import fitnesse.wikitext.parser.Maybe;
import fitnesse.wikitext.shared.MarkUpConfig;
import fitnesse.wikitext.shared.MarkUpSystem;
import fitnesse.wikitext.shared.ParsingPage;
import fitnesse.wikitext.shared.SourcePage;

import java.util.Collection;
import java.util.Collections;

public class MarkUp {
  public MarkUp() {
    page2 = new ParsingPage(new FakeSourcePage());
    page3 = new ParsingPage(new FakeSourcePage());
    system2 = MarkUpConfig.make("2");
    system3 = MarkUpConfig.make("3");
  }
  public String with2(String input) {
    return system2.parse(page2, input).translateToHtml().trim();
  }

  public String with3(String input) {
    return system3.parse(page3, input).translateToHtml().trim();
  }

  private final MarkUpSystem system2;
  private final MarkUpSystem system3;
  private final ParsingPage page2;
  private final ParsingPage page3;

  private static class FakeSourcePage implements SourcePage {
    @Override public String getName() { return ""; }
    @Override public String getPath() { return ""; }
    @Override public String getFullPath() { return ""; }
    @Override public String getContent() { return ""; }
    @Override public boolean targetExists(String wikiWordPath) { return false; }
    @Override public String makeFullPathOfTarget(String wikiWordPath) { return ""; }
    @Override public String findParentPath(String targetName) { return ""; }
    @Override public Maybe<SourcePage> findIncludedPage(String pageName) { return Maybe.nothingBecause(""); }
    @Override public Collection<SourcePage> getChildren() { return Collections.emptyList(); }
    @Override public boolean hasProperty(String propertyKey) { return false; }
    @Override public String getProperty(String propertyKey) { return ""; }
    @Override public int compareTo(SourcePage sourcePage) { return 0; }
  }
}
