package fitnesse.wikitext.parser3;

@FunctionalInterface
public interface ParseRule {
  Symbol parse(Parser parser);
}
