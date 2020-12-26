package fitnesse.wikitext.shared;

import fitnesse.wikitext.parser.MarkUpSystemV2;
import fitnesse.wikitext.parser3.MarkUpSystemV3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkUpConfig {
  public static String version = "2";

  public static MarkUpSystem make() {
    return MarkUpConfig.version.equals("3") ? new MarkUpSystemV3() : new MarkUpSystemV2();
    //return new MarkUpSystemV2();
    //return new MarkUpSystemV3();
  }

  public static void addDecorator(String syntaxType, SyntaxNodeDecorator decorator) {
    String key = syntaxType.toLowerCase();
    if (!decorators.containsKey(key)) {
      decorators.put(key, new ArrayList<>());
    }
    //todo: check if already added
    decorators.get(key).add(decorator);
  }

  public static void removeDecorator(String syntaxType, SyntaxNodeDecorator decorator) {
    String key = syntaxType.toLowerCase();
    if (!decorators.containsKey(key)) return;
    decorators.get(key).remove(decorator);
  }

  public static void decorate(SyntaxNode node, ParsingPage page) {
    String key = node.getTypeName().toLowerCase();
    if (!decorators.containsKey(key)) return;
    for (SyntaxNodeDecorator decorator: decorators.get(key)) {
      decorator.decorate(node, page);
    }
  }

  private static final Map<String, List<SyntaxNodeDecorator>> decorators = new HashMap<>();
}
