package fitnesse.wikitext.parser3;

import fitnesse.wikitext.SourcePage;
import fitnesse.wikitext.parser.Maybe;

import java.util.Collection;

public class FakeSourcePage implements SourcePage {
  @Override
  public String getName() {
    return "FakeName";
  }

  @Override
  public String getFullName() {
    return null;
  }

  @Override
  public String getPath() {
    return null;
  }

  @Override
  public String getFullPath() {
    return null;
  }

  @Override
  public String getContent() {
    return null;
  }

  @Override
  public boolean targetExists(String wikiWordPath) {
    return true;
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
    return null;
  }

  @Override
  public Collection<SourcePage> getChildren() {
    return null;
  }

  @Override
  public boolean hasProperty(String propertyKey) {
    return false;
  }

  @Override
  public String getProperty(String propertyKey) {
    return propertyKey + "Value";
  }

  @Override
  public int compareTo(SourcePage sourcePage) {
    return 0;
  }
}
