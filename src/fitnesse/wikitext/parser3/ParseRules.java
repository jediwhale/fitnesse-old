package fitnesse.wikitext.parser3;

import fitnesse.wikitext.VariableStore;

import java.util.HashMap;
import java.util.Map;

class ParseRules {
  static Map<TokenType, ParseRule> make(VariableStore variables, External external) {
    Map<TokenType, ParseRule> rules = new HashMap<>();
    rules.put(TokenType.ALIAS_START, Alias::parse);
    rules.put(TokenType.ANCHOR_NAME, Keyword.parseWord(SymbolType.ANCHOR_NAME));
    rules.put(TokenType.ANCHOR_REFERENCE, Keyword.parseWord(SymbolType.ANCHOR_REFERENCE));
    rules.put(TokenType.BOLD, Pair.parse(SymbolType.BOLD));
    rules.put(TokenType.BOLD_ITALIC, Pair.parse(SymbolType.BOLD_ITALIC));
    rules.put(TokenType.COMMENT, Comment::parse);
    rules.put(TokenType.DEFINE, parser -> Variable.parsePut(parser, variables));
    rules.put(TokenType.EXPRESSION_START, Expression::parse);
    rules.put(TokenType.INCLUDE, parser -> Include.parse(parser, external, variables));
    rules.put(TokenType.ITALIC, Pair.parse(SymbolType.ITALIC));
    rules.put(TokenType.LAST_MODIFIED, makeType(SymbolType.LAST_MODIFIED));
    rules.put(TokenType.LINK, Link::parse);
    rules.put(TokenType.LITERAL_START, Literal::parse);
    rules.put(TokenType.NESTING_START, Nesting::parse);
    rules.put(TokenType.NEW_LINE, makeType(SymbolType.NEW_LINE));
    rules.put(TokenType.PATH, Path::parse);
    rules.put(TokenType.PLAIN_TEXT_TABLE_START, Table::parsePlain);
    rules.put(TokenType.PREFORMAT_START, Preformat::parse);
    rules.put(TokenType.SEE, Keyword.parse(SymbolType.SEE));
    rules.put(TokenType.STRIKE, Pair.parse(SymbolType.STRIKE));
    rules.put(TokenType.STYLE, Style::parse);
    rules.put(TokenType.TABLE_START, Table::parseStandard);
    rules.put(TokenType.VARIABLE, parser -> Variable.parseGet(parser, variables));
    return rules;
  }

  static ParseRule makeType(SymbolType symbolType) {
    return parser -> {
      return new LeafSymbol(symbolType, parser.advance().getContent());
    };
  }
}
