package fitnesse.wikitext.parser3;

import fitnesse.wikitext.SourcePage;
import fitnesse.wikitext.parser.TestSourcePage;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class MarkUpSystemV3Test {
  @Test public void changesWikiWord() {
    SourcePage source = new TestSourcePage().withContent("here is a WikiPage name");
    String after = new MarkUpSystemV3().changeReferences(source, before -> Optional.of("ChangedPage"));
    Assert.assertEquals("here is a ChangedPage name", after);
  }

  @Test public void returnsOriginal() {
    SourcePage source = new TestSourcePage().withContent("here is a WikiPage name");
    String after = new MarkUpSystemV3().changeReferences(source, before -> Optional.empty());
    Assert.assertEquals("here is a WikiPage name", after);
  }
}
