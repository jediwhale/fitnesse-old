package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class ImageTest {
  @Test public void scansImage() {
    assertScansWord("!img", "Image");
    assertScansWord("!img-r", "Image");
    assertScansWord("!img-l", "Image");
  }

  @Test public void parses() {
    assertParses("IMAGE(TEXT=url)", "!img url");
    assertParses("IMAGE(TEXT=PageOne)", "!img PageOne");
    assertParses("IMAGE(TEXT=url)[image=left]", "!img-l url");
    assertParses("IMAGE(TEXT=url)[image=right]", "!img-r url");
  }

  @Test public void parsesOptions() {
    assertParses("IMAGE(TEXT=name)[-w=640]", "!img -w 640 name");
  }

  @Test public void translates() {
    assertTranslates("<img src=\"url\"/>", "!img url");
    assertTranslates("<img src=\"url\" class=\"left\"/>", "!img-l url");
    assertTranslates("<img src=\"url\" class=\"right\"/>", "!img-r url");
  }

  @Test public void TranslatesOptions() {
    assertTranslates("<img src=\"name\" width=\"640\"/>", "!img -w 640 name");
  }
}
