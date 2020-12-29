package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.VariableSource;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class ContentTest {
  @Test public void singleSource() {
    Content content = makeContent("abc");
    assertNext(content, 'a', 0);
    assertNext(content, 'b', 1);
    assertNext(content, 'c', 2);
    Assert.assertFalse(content.more());
  }

  @Test public void multipleSources() {
    Content content = makeContent("ac");
    assertNext(content, 'a', 0);
    content.insertVariable("v");
    assertNext(content, Nesting.START, -1);
    assertNext(content, 'v', -1);
    assertNext(content, Nesting.END, -1);
    assertNext(content, 'c', 1);
    Assert.assertFalse(content.more());
  }

  @Test public void singleSourceStartsWith() {
    Content content = makeContent("abc");
    Assert.assertTrue(content.startsWith("ab"));
    content.advance(1);
    Assert.assertTrue(content.startsWith("bc"));
    content.advance(1);
    Assert.assertFalse(content.startsWith("cd"));
  }

  @Test public void multipleSourceStartsWith() {
    Content content = makeContent("ac");
    Assert.assertTrue(content.startsWith("ac"));
    content.advance(1);
    content.insertVariable("v");
    Assert.assertTrue(content.startsWith(Nesting.START + "v" + Nesting.END + "c"));
    content.advance(3);
    Assert.assertFalse(content.startsWith("cd"));
  }

  private void assertNext(Content source, char expected, int current) {
    Assert.assertTrue(source.more());
    Assert.assertEquals(current, source.getCurrent());
    Assert.assertEquals(expected, source.advance());
  }

  private Content makeContent(String input) {
    return new Content(input, new FakeVariable());
  }

  private static class FakeVariable implements VariableSource {
    @Override
    public Optional<String> findVariable(String name) {
      return Optional.of(name);
    }
  }
}
