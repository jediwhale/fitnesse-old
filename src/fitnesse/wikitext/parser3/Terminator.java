package fitnesse.wikitext.parser3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

class Terminator {
  static final Terminator END_LINE = new Terminator(type -> type == DelimiterType.NEW_LINE || type == TokenType.END);
  static final Terminator NONE = new Terminator(type -> false);

  static Terminator make(Token start) { return make(start, ""); }

  static Terminator make(Terminator first, Terminator second) {
    return new Terminator(type -> first.matches(type) || second.matches(type), first.description, first.prefix);
  }

  static Terminator make(Token start, String prefix) {
    TokenType startType = start.getType();
    TokenType endType = endTypes.getOrDefault(startType, startType);
    return new Terminator(candidate -> candidate == endType, endType.getMatch(), prefix + start.getContent());
  }

  Terminator(TokenType type) {
    this(candidate -> candidate == type, type.getMatch(), "");
  }

  Terminator(Predicate<TokenType> matcher) {
    this(matcher, "", "");
  }

  Terminator(Predicate<TokenType> matcher, String description, String prefix) {
    this.description = description;
    this.prefix = prefix;
    this.matcher = matcher;
  }

  boolean matches(TokenType type) { return matcher.test(type); }

  String missing() { return prefix + (prefix.length() > 0 ? " " : "") + "Missing terminator: " + description; }

  private final String description;
  private final String prefix;
  private final Predicate<TokenType> matcher;

  private static final Map<TokenType, TokenType> endTypes = makeEndTypes();

  private static Map<TokenType, TokenType> makeEndTypes() {
    Map<TokenType, TokenType> map = new HashMap<>();
    map.put(DelimiterType.ALIAS_START, DelimiterType.ALIAS_MIDDLE);
    map.put(DelimiterType.ALIAS_MIDDLE, DelimiterType.ALIAS_END);
    map.put(DelimiterType.BRACE_START, DelimiterType.BRACE_END);
    map.put(DelimiterType.BRACKET_START, DelimiterType.BRACKET_END);
    map.put(DelimiterType.EXPRESSION_START, DelimiterType.EXPRESSION_END);
    map.put(DelimiterType.LITERAL_START, DelimiterType.LITERAL_END);
    map.put(DelimiterType.NESTING_START, DelimiterType.NESTING_END);
    map.put(DelimiterType.NESTING_PSEUDO_START, DelimiterType.NESTING_PSEUDO_END);
    map.put(DelimiterType.PARENTHESIS_START, DelimiterType.PARENTHESIS_END);
    map.put(DelimiterType.PREFORMAT_START, DelimiterType.PREFORMAT_END);
    return Collections.unmodifiableMap(map);
  }
}
