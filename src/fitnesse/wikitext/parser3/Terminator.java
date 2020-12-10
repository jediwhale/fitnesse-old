package fitnesse.wikitext.parser3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

class Terminator {
  static final Terminator END_LINE = new Terminator(type -> type == TokenType.NEW_LINE || type == TokenType.END);
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
    map.put(TokenType.ALIAS_START, TokenType.ALIAS_MIDDLE);
    map.put(TokenType.ALIAS_MIDDLE, TokenType.ALIAS_END);
    map.put(TokenType.BRACE_START, TokenType.BRACE_END);
    map.put(TokenType.BRACKET_START, TokenType.BRACKET_END);
    map.put(TokenType.EXPRESSION_START, TokenType.EXPRESSION_END);
    map.put(TokenType.LITERAL_START, TokenType.LITERAL_END);
    map.put(TokenType.NESTING_START, TokenType.NESTING_END);
    map.put(TokenType.NESTING_PSEUDO_START, TokenType.NESTING_PSEUDO_END);
    map.put(TokenType.PARENTHESIS_START, TokenType.PARENTHESIS_END);
    map.put(TokenType.PREFORMAT_START, TokenType.PREFORMAT_END);
    return Collections.unmodifiableMap(map);
  }
}
