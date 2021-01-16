package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class ImageTest {
  @Test public void scans() {
    assertScansKeyword("!img", "Image");
    assertScansKeyword("!img-r", "Image");
    assertScansKeyword("!img-l", "Image");
  }

  @Test public void parses() {
    assertParses("IMAGE(TEXT=url)", "!img url");
    assertParses("IMAGE(TEXT=PageOne)", "!img PageOne");
    assertParses("IMAGE(TEXT=http://something)", "!img http://something");
    assertParses("IMAGE(TEXT=url)[image=left]", "!img-l url");
    assertParses("IMAGE(TEXT=url)[image=right]", "!img-r url");
  }

  @Test public void parsesOptions() {
    assertParses("IMAGE(TEXT=name)[-w=640]", "!img -w 640 name");
    assertParses("IMAGE(TEXT=name)[-b=2]", "!img -b 2 name");
    assertParses("IMAGE(TEXT=name)[-m=1]", "!img -m 1 name");
    assertParses("IMAGE(TEXT=name)[-b=2,-m=1,-w=640]", "!img -w 640 -m 1 -b 2 name");
  }

  @Test public void translates() {
    assertTranslates("<img src=\"url\"/>", "!img url");
    assertTranslates("<img src=\"http://mysite.com/something.jpg\"/>", "!img http://mysite.com/something.jpg");
    assertTranslates("<img src=\"url\" class=\"left\"/>", "!img-l url");
    assertTranslates("<img src=\"url\" class=\"right\"/>", "!img-r url");
  }

  @Test public void translatesOptions() {
    assertTranslates("<img src=\"name\" width=\"640\"/>", "!img -w 640 name");
    assertTranslates("<img src=\"name\" style=\"border:2px solid black;\"/>", "!img -b 2 name");
    assertTranslates("<img src=\"name\" style=\"margin:1px;\"/>", "!img -m 1 name");
    assertTranslates("<img src=\"name\" width=\"640\" style=\"border:2px solid black;margin:1px;\"/>", "!img -w 640 -m 1 -b 2 name");
  }
}
