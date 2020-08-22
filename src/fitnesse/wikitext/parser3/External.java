package fitnesse.wikitext.parser3;

public interface External {
  String fullPath(String input);
  boolean exists(String path);
}
