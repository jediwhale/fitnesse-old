package fitnesse.wikitext.shared;

import java.util.List;

public interface SyntaxNode extends PropertyStore {
  String getTypeName();
  String getContent();
  List<? extends SyntaxNode> getChildren();

  default String getAllContent() {
    StringBuilder result = new StringBuilder(getContent());
    for (SyntaxNode child: getChildren()) result.append(child.getAllContent());
    return result.toString();
  }
}
