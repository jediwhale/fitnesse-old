package fitnesse.wikitext.parser3;

import fitnesse.wikitext.parser.Maybe;
import fitnesse.wikitext.shared.SourcePage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FakeSourcePage implements SourcePage {
  public FakeSourcePage() {}

  public FakeSourcePage(String name) {
    this.name = name;
    this.content = "fake content";
  }

  public FakeSourcePage(String name, String content) {
    this.name = name;
    this.content = content;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPath() {
    return null;
  }

  @Override
  public String getFullPath() {
    return "FullPath";
  }

  @Override
  public String getContent() {
    return content;
  }

  @Override
  public boolean targetExists(String wikiWordPath) {
    return !wikiWordPath.contains("New");
  }

  @Override
  public String makeFullPathOfTarget(String wikiWordPath) {
    return "Fake." + wikiWordPath;
  }

  @Override
  public String findParentPath(String targetName) {
    return null;
  }

  @Override
  public Maybe<SourcePage> findIncludedPage(String pageName) {
    String key = name + "." + pageName;
    return pages.containsKey(key) ? new Maybe<>(pages.get(key)) : Maybe.nothingBecause(key + " not found");
  }

  @Override
  public Collection<SourcePage> getChildren() {
    return null;
  }

  @Override
  public boolean hasProperty(String propertyKey) {
    return properties.containsKey(propertyKey);
  }

  @Override
  public String getProperty(String propertyKey) {
    return properties.get(propertyKey);
  }

  @Override
  public int compareTo(SourcePage sourcePage) {
    return 0;
  }

  public static void addPage(String name, String content) {
    pages.put(name, new FakeSourcePage(name, content));
  }

  public String name = "FakeName";
  public String content;
  public Map<String, String> properties = new HashMap<>();
  public static final HashMap<String, FakeSourcePage> pages = new HashMap<>();
}
