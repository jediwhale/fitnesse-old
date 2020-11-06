package fitnesse.wikitext.parser3;

import fitnesse.wikitext.shared.PropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

class Translate implements TranslateRule {
  public static Translate with(BiFunction<String[], PropertySource, String> method) {
    return new Translate(method);
  }

  public static Translate with(Function<String[], String> method) {
    return new Translate((strings, source) -> method.apply(strings));
  }

  public static Translate with(Supplier<String> method) {
    return new Translate((strings, source) -> method.get());
  }

  Translate(BiFunction<String[], PropertySource, String> method) {
    this.method = method;
  }

  @Override
  public String translate(Symbol symbol, Translator translator) {
    return method.apply(arguments.stream().map(a -> a.translate(symbol, translator)).toArray(String[]::new), symbol);
  }

  Translate text(String content) {
    arguments.add((s,t) -> content);
    return this;
  }

  Translate children() {
    arguments.add(Symbol::translateChildren);
    return this;
  }

  Translate content() {
    arguments.add(Symbol::translateContent);
    return this;
  }

  Translate branch(int index) {
    arguments.add((s,t) -> s.translateBranch(t, index));
    return this;
  }

  Translate leaf() {
    arguments.add((s,t) -> s.getContent());
    return this;
  }

  private final BiFunction<String[], PropertySource, String> method;
  private final List<TranslateRule> arguments = new ArrayList<>();
}
