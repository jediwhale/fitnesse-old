package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.wikitext.shared.VariableStore;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

class Define {
  static Symbol parse(Parser parser, VariableStore variables) {
    Symbol result = new BranchSymbol(SymbolType.DEFINE);
    if (!parser.peek(2).isWord()) {
      return parser.makeError("Name must be alphanumeric", 2);
    }
    if (!parser.peek(3).isType(DelimiterType.BLANK_SPACE)) {
      return parser.makeError("Missing blank space", 3);
    }
    if (parser.peek(4).isType(TokenType.TEXT)) {
      if (!variables.findVariable(parser.peek(4).getContent()).isPresent()) {
        return parser.makeError("Variable not found", 4);
      }
    }
    else if (!parser.peek(4).isStartType()) {
      return parser.makeError("Expected { ( or [", 4);
    }
    parser.advance();
    parser.advance(); //todo: check blank
    result.add(parser.parseCurrent());
    parser.advance(); //todo: check blank


    Function<String, Symbol> mapText = text -> {
      result.add(Symbol.text(text));
      variables.putVariable(result.getContent(0), result.getContent(1));
      return result;
    };

    BiFunction<String, String, Symbol> mapError = (text, error) ->
      Symbol.makeList(mapText.apply(text), Symbol.error(error));

    if (parser.peek(0).isType(TokenType.TEXT)) {
      return mapText.apply(variables.findVariable(parser.advance().getContent()).orElse(""));
    }

    return parser
      .withTokenTypes(DEFINE_TYPES)
      .collectText(Terminator.make(parser.advance()), mapText, mapError);
  }

  static String translate(Symbol symbol, Translator translator) {
    return HtmlTag.name("span")
      .attribute("class", "meta")
      .text("variable defined: " + symbol.getContent(0) + "=" + symbol.getContent(1))
      .htmlInline();
  }

  private static final TokenTypes DEFINE_TYPES = new TokenTypes(
    Arrays.asList(
      DelimiterType.VARIABLE_TOKEN, //must be first
      DelimiterType.LITERAL_START,

      //todo: drop these
      //these end in ! and if omitted, could form literal-start if followed by -
      DelimiterType.COLLAPSIBLE_END,
      DelimiterType.LITERAL_END,
      DelimiterType.NESTING_END,
      DelimiterType.PLAIN_TEXT_TABLE_END,

      //todo: use only the one for the current terminator
      //possible terminators for !define
      DelimiterType.BRACE_END,
      DelimiterType.BRACKET_END,
      DelimiterType.PARENTHESIS_END
    ));
}
