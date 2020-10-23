package fitnesse.wikitext.parser3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class Translate implements TranslateRule {
  public static Translate with(Function<String[], String> method) {
    return new Translate(method);
  }

  Translate(Function<String[], String> method) {
    this.method = method;
  }

  @Override
  public String translate(Symbol symbol, Translator translator) {
    return method.apply(arguments.stream().map(a -> a.translate(symbol, translator)).toArray(String[]::new));
  }

  Translate text(String content) {
    arguments.add((s,t) -> content);
    return this;
  }

  Translate content() {
    arguments.add(Symbol::translateContent);
    return this;
  }

  private final Function<String[], String> method;
  private final List<TranslateRule> arguments = new ArrayList<>();
}
