package fitnesse.wikitext.parser3;

import org.junit.Assert;
import org.junit.Test;

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
    content.insert(new ContentSegment("b"));
    assertNext(content, 'b', -1);
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
    content.insert(new ContentSegment("b"));
    Assert.assertTrue(content.startsWith("bc"));
    content.advance(1);
    Assert.assertFalse(content.startsWith("cd"));
  }

  private void assertNext(Content source, char expected, int current) {
    Assert.assertTrue(source.more());
    Assert.assertEquals(current, source.getCurrent());
    Assert.assertEquals(expected, source.advance());
  }

  private Content makeContent(String input) {
    return new Content(input, ContentSegment::new);
  }
}
