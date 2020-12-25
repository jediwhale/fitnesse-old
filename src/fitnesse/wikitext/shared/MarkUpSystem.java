package fitnesse.wikitext.shared;

import fitnesse.wikitext.parser.MarkUpSystemV2;
import fitnesse.wikitext.parser3.MarkUpSystemV3;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface MarkUpSystem {
  SyntaxTree parse(ParsingPage page, String content);
  String variableValueToHtml(ParsingPage page, String variableValue);
  void findWhereUsed(SourcePage page, Consumer<String> takeWhereUsed);
  String changeReferences(SourcePage page, Function<String, Optional<String>> changeReference);

  static MarkUpSystem make() {
    return MarkUpConfig.version.equals("3") ? new MarkUpSystemV3() : new MarkUpSystemV2(); }
}
