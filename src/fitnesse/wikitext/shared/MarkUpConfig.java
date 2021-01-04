package fitnesse.wikitext.shared;

import fitnesse.wikitext.parser.MarkUpSystemV2;
import fitnesse.wikitext.parser3.MarkUpSystemV3;

public class MarkUpConfig {
  public static String version = "2";

  public static MarkUpSystem make() {
    return MarkUpConfig.version.equals("3") ? new MarkUpSystemV3() : new MarkUpSystemV2();
    //return new MarkUpSystemV2();
    //return new MarkUpSystemV3();
  }
}
