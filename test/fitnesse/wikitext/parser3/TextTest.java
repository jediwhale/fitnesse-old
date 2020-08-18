package fitnesse.wikitext.parser3;

import org.junit.Test;

import static org.junit.Assert.*;

public class TextTest {
  @Test
  public void splitWithNoSeparator() {
    assertTrue(new Text("hi").matchAll(".", this::isHi));
    assertFalse(new Text("hithere").matchAll(".", this::isHi));
  }

  @Test
  public void splitWithSeparator() {
    assertTrue(new Text("hi.hi").matchAll(".", this::isHi));
    assertTrue(new Text("hi.hi.hi.hi").matchAll(".", this::isHi));
    assertFalse(new Text("hi.there").matchAll(".", this::isHi));
    assertFalse(new Text("there.hi").matchAll(".", this::isHi));
    assertFalse(new Text("hi.h").matchAll(".", this::isHi));
    assertFalse(new Text("hi.hip").matchAll(".", this::isHi));
  }

  @Test
  public void applyWithNoSeparator() {
    assertApplies("hithere", "hithere");
  }

  @Test
  public void applyWithSeparator() {
    assertApplies("hi.there.bob", "hitherebob");
  }

  private void assertApplies(String input, String expected) {
    StringBuilder result = new StringBuilder();
    new Text(input).applyAll(".", text -> result.append(text.content()));
    assertEquals(expected, result.toString());
  }

  boolean isHi(Text text) {
    return text.equals("hi");
  }
}
