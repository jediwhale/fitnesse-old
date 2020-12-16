package fitnesse.wikitext.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkUpConfig {
  public static void addDecorator(String syntaxType, SyntaxNodeDecorator decorator) {
    if (!decorators.containsKey(syntaxType)) {
      decorators.put(syntaxType, new ArrayList<>());
    }
    decorators.get(syntaxType).add(decorator);
  }

  public static void removeDecorator(String syntaxType, SyntaxNodeDecorator decorator) {
    if (!decorators.containsKey(syntaxType)) return;
    decorators.get(syntaxType).remove(decorator);
  }

  public static void decorate(SyntaxNode node, ParsingPage page) {
    if (!decorators.containsKey(node.getTypeName())) return;
    for (SyntaxNodeDecorator decorator: decorators.get(node.getTypeName())) {
      decorator.decorate(node, page);
    }
  }

  private static final Map<String, List<SyntaxNodeDecorator>> decorators = new HashMap<>();
}
