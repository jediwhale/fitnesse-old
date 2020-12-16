package fitnesse.wikitext.shared;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PropertyStoreTest {

  private final TestStore testStore = new TestStore();

  @Test
  public void should_have_no_class_prop_by_default() {
    assertThat(testStore.hasProperty(Names.CLASS), is(false));
    assertThat(testStore.findProperty(Names.CLASS, ""), is(""));
  }

  @Test
  public void should_have_class_prop_when_appended() {
    testStore.appendProperty(Names.CLASS, "abc");
    assertThat(testStore.hasProperty(Names.CLASS), is(true));
    assertThat(testStore.findProperty(Names.CLASS, ""), is("abc"));
  }

  @Test
  public void should_have_concatenated_values_when_multiple_appended() {
    testStore.appendProperty(Names.CLASS, "abc");
    testStore.appendProperty(Names.CLASS, "def");
    testStore.appendProperty(Names.CLASS, "ghi");
    assertThat(testStore.findProperty(Names.CLASS, ""), is("abc def ghi"));
  }

  @Test
  public void should_not_append_same_value_twice() {
    testStore.appendProperty(Names.CLASS, "abc");
    testStore.appendProperty(Names.CLASS, "def");
    testStore.appendProperty(Names.CLASS, "abc");
    assertThat(testStore.findProperty(Names.CLASS, ""), is("abc def"));
  }

  @Test
  public void should_append_similiar_values() {
    testStore.appendProperty(Names.CLASS, "abc");
    testStore.appendProperty(Names.CLASS, "ab");
    testStore.appendProperty(Names.CLASS, "c");
    assertThat(testStore.findProperty(Names.CLASS, ""), is("abc ab c"));
  }

  private static class TestStore implements PropertyStore {

    @Override
    public void putProperty(String key, String value) {
      map.put(key, value);
    }

    @Override
    public Optional<String> findProperty(String key) {
      return Optional.ofNullable(map.get(key));
    }

    @Override
    public boolean hasProperty(String key) {
      return map.containsKey(key);
    }

    final Map<String, String> map = new HashMap<>();
  }
}
