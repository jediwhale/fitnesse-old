package fitnesse.wikitext.parser3;

class HashTable {
  static void scan(TokenSource source) {
    source.use(TokenTypes.HASH_TABLE_TYPES, type -> type == TokenType.BRACE_END);
  }
}
