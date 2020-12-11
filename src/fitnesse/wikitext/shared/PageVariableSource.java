package fitnesse.wikitext.shared;

import fitnesse.wikitext.SourcePage;
import fitnesse.wikitext.VariableSource;

import java.util.Optional;

public class PageVariableSource implements VariableSource {
  public PageVariableSource(SourcePage page) {
    this.page = page;
  }

  @Override
  public Optional<String> findVariable(String name) {
    String value;
    if (name.equals("PAGE_NAME"))
      value = page.getName();
    else if (name.equals("PAGE_PATH"))
      value = page.getPath();
    else
      return Optional.empty();

    return Optional.ofNullable(value);
  }

  private final SourcePage page;
}
