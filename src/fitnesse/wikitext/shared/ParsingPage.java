package fitnesse.wikitext.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The page represents wiki page in the course of being parsed.
 */
//todo: interface segregate the page stuff
public class ParsingPage implements VariableStore, PageSources {

  private final SourcePage page;
  private final SourcePage namedPage; // included page
  private final VariableSource variableSource;
  private final Cache cache;

  public ParsingPage(SourcePage page) {
    this(page, new Cache());
  }

  public ParsingPage(SourcePage page, VariableSource variableSource, Cache cache) {
    this(page, page, variableSource, cache);
  }

  private ParsingPage(SourcePage page, Cache cache) {
    this(page, page, cache, cache);
  }

  private ParsingPage(SourcePage page, SourcePage namedPage, VariableSource variableSource, Cache cache) {
    this.page = page;
    this.namedPage = namedPage;
    this.variableSource = new CompositeVariableSource(
            new PageVariableSource(namedPage),
            variableSource);
    this.cache = cache;
  }

  public ParsingPage copyForNamedPage(SourcePage namedPage) {
    return new ParsingPage(
            this.page,
            namedPage,
            this.variableSource,
            this.cache);
  }

  @Override
  public SourcePage getPage() {
    return page;
  }

  @Override
  public SourcePage getNamedPage() {
    return namedPage;
  }

  @Override
  public int nextId() { return cache.nextId(); }

  @Override
  public void putVariable(String name, String value) {
    cache.putVariable(name, value);
  }

  @Override
  public Optional<String> findVariable(String name) {
    return variableSource != null ? variableSource.findVariable(name) : Optional.empty();
  }

  public static class Cache implements VariableStore {

    private final Map<String, String> cache;
    private int id;

    public Cache() {
      this(new HashMap<>());
    }

    public Cache(Map<String, String> cache) {
      this.cache = cache;
    }

    @Override
    public void putVariable(String name, String value) {
      cache.put(name, value);
    }

    @Override
    public Optional<String> findVariable(String name) {
      return Optional.ofNullable(cache.get(name));
    }

    @Override
    public int nextId() {
      return id++;
    }
  }
}
