package fitnesse.wikitext.parser3;

public interface External {
  @FunctionalInterface
  interface LinkFormatter {
    String formatLink(String path);
  }

  String translateLink(String path, LinkFormatter existingPage, LinkFormatter newPage);
}
