package fitnesse.wikitext.parser3;

import java.util.Optional;

@FunctionalInterface
interface MatchContent {
  Optional<String> check(Content content);

  static MatchContent matchAll(MatchContent... matchItems) {
    return content -> {
      Content trial = new Content(content);
      StringBuilder result = new StringBuilder();
      for (MatchContent item : matchItems) {
        Optional<String> itemResult = item.check(trial);
        if (!itemResult.isPresent()) return Optional.empty();
        result.append(itemResult.get());
      }
      content.advance(result.length());
      return Optional.of(result.toString());
    };
  }

  static MatchContent matchOne(MatchContent... matchItems) {
    return content -> {
      for (MatchContent item : matchItems) {
        Optional<String> result = item.check(content);
        if (result.isPresent()) return result;
      }
      return Optional.empty();
    };
  }

  static MatchContent blank() {
    return content -> {
      StringBuilder result = new StringBuilder();
      while (content.isBlankSpace()) {
        result.append(content.advance());
      }
      return result.length() > 0 ? Optional.of(result.toString()) : Optional.empty();
    };
  }

  static MatchContent digit() {
    return content -> content.isDigit() ? Optional.of(Character.toString(content.advance())): Optional.empty();
  }

  static MatchContent end() {
    return content ->
      (!content.more()
        || DelimiterType.NESTING_END.check(content)
        || DelimiterType.NESTING_PSEUDO_END.check(content))
        ? Optional.of("")
        : Optional.empty();
  }

  static MatchContent endWith(MatchContent end) {
    return content -> {
      StringBuilder result = new StringBuilder();
      while (content.more()) {
        Optional<String> ended = end.check(content);
        if (ended.isPresent()) {
          result.append(ended.get());
          break;
        }
        result.append(content.advance());
      }
      return Optional.of(result.toString());
    };
  }

  static MatchContent ignoreBlank() {
    return content -> {
      StringBuilder result = new StringBuilder();
      while (content.isBlankSpace()) {
        result.append(content.advance());
      }
      return Optional.of(result.toString());
    };
  }

  static MatchContent newLine() {
    return matchOne(text("\r\n"), text("\n"), text("\r"));
  }

  static MatchContent notText(String text) {
    return content -> content.startsWith(text) ? Optional.empty() : Optional.of("");
  }

  static MatchContent repeat(String repeat) {
    return content -> {
      StringBuilder result = new StringBuilder();
      while (content.startsWith(repeat)) {
        result.append(repeat);
        content.advance(repeat.length());
      }
      return result.length() > 0 ? Optional.of(result.toString()) : Optional.empty();
    };
  }

  static MatchContent startLine() {
    return content -> (content.isStartLine()) ? Optional.of("") : Optional.empty();
  }

  static MatchContent text(String text) {
    return content -> {
      if (content.startsWith(text)) {
        content.advance(text.length());
        return Optional.of(text);
      }
      return Optional.empty();
    };
  }

  static MatchContent word(String match) {
    return matchAll(text(match), blank());
  }

  static MatchContent variableValue() {
    return content -> {
      scanVariableName(content).ifPresent(content::insertVariable);
      return Optional.empty();
    };
  }

  static MatchContent variableToken() {
    return content -> scanVariableName(content).map(name -> VARIABLE_START + name + VARIABLE_END);
  }

  static Optional<String> scanVariableName(Content content) {
    if (!content.startsWith(VARIABLE_START)) return Optional.empty();
    Content trial = new Content(content);
    StringBuilder result = new StringBuilder();
    trial.advance(VARIABLE_START.length());
    if (trial.startsWith(EXPRESSION_INDICATOR)) return Optional.empty();
    do {
      if (!trial.more()) return Optional.empty();
      result.append(trial.advance());
    } while (!trial.startsWith(VARIABLE_END));
    content.advance(VARIABLE_START.length() + result.length() + VARIABLE_END.length());
    return Optional.of(result.toString());
  }

  String EXPRESSION_INDICATOR = "=";
  String VARIABLE_START = "${";
  String VARIABLE_END = "}";
}
