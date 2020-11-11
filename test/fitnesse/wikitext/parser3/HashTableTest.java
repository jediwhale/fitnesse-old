package fitnesse.wikitext.parser3;

import org.junit.Test;

import static fitnesse.wikitext.parser3.Helper.*;

public class HashTableTest {
  @Test public void scans() {
    assertScans("HashTable=!{,Text=key1,Colon=:,Text=value1,Comma=,,Text=key2,Colon=:,Text=value2,BraceEnd=}",
      "!{key1:value1,key2:value2}");
  }

  @Test public void parses() {
    assertParses("HASH_TABLE(LIST(LIST(TEXT=key),LIST(TEXT=value)))", "!{key:value}");
    assertParses(
      "HASH_TABLE(LIST(LIST(TEXT=key1),LIST(TEXT=value1)),LIST(LIST(TEXT=key2),LIST(TEXT=value2)))",
      "!{key1:value1,key2:value2}");
  }

  @Test public void translates() {
    assertTranslates(
      "<table class=\"hash_table\">\n" +
      "\t<tr class=\"hash_row\">\n" +
      "\t\t<td class=\"hash_key\">key</td>\n" +
      "\t\t<td class=\"hash_value\">value</td>\n" +
      "\t</tr>\n" +
      "</table>\n",
      "!{key:value}");
  }
}
