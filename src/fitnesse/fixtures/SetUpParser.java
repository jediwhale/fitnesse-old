package fitnesse.fixtures;

import fit.Fixture;
import fit.Parse;
import fitnesse.wikitext.shared.MarkUpConfig;

public class SetUpParser extends Fixture {
  public SetUpParser() {}

  public SetUpParser(String version) {
    MarkUpConfig.version = version;
  }

  public void doTable(Parse table) {
    MarkUpConfig.version = args[0];
  }
}
