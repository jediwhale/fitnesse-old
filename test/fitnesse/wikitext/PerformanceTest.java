package fitnesse.wikitext;

import java.util.ArrayList;
import java.util.List;

import fitnesse.slim.protocol.SlimDeserializer;

import fitnesse.slim.protocol.SlimSerializer;

import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiSourcePage;
import fitnesse.wikitext.parser.*;
import fitnesse.wikitext.parser3.MarkUpSystemV3;
import fitnesse.wikitext.shared.ParsingPage;

import fitnesse.wikitext.shared.SyntaxTree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PerformanceTest {
    private final String tablePageContent;
    private final String definePageContent;

    public PerformanceTest() {
        StringBuilder table = new StringBuilder();
        StringBuilder define = new StringBuilder();
        for (int i = 0; i < 2000; i++) {
            table.append("|aaaaaaaaaa|bbbbbbbbbb|cccccccccc|dddddddddd|eeeeeeeeee|ffffffffff|gggggggggg|hhhhhhhhhh|iiiiiiiiiii|jjjjjjjjjj|kkkkkkkkkk|lllllllllll|mmmmmmmmmm|nnnnnnnnnn|oooooooooo|pppppppppp|qqqqqqqqqq|rrrrrrrrrr|ssssssssss|tttttttttt|uuuuuuuuuu|vvvvvvvvvv|wwwwwwwwww|xxxxxxxxxx|yyyyyyyyyy|zzzzzzzzzz|\n");
            define.append("!define variable").append(i).append(" {aaaaaaaaaa bbbbbbbbbb cccccccccc dddddddddd eeeeeeeeee ffffffffff gggggggggg hhhhhhhhhh iiiiiiiiii jjjjjjjjjj kkkkkkkkkk llllllllll mmmmmmmmmm nnnnnnnnnn oooooooooo pppppppppp qqqqqqqqqq rrrrrrrrrr ssssssssss tttttttttt uuuuuuuuuu vvvvvvvvvv wwwwwwwwww xxxxxxxxxx yyyyyyyyyy zzzzzzzzzz}\n");
        }
        tablePageContent = table.toString();
        definePageContent = define.toString();
    }

    @Test
    public void NewParserTable() {
      runParser2("big table2", tablePageContent);
      runParser3("big table3", tablePageContent);
    }

    @Test
    public void NewParserDefine() {
      runParser2("big define2", definePageContent);
      runParser3("big define3", definePageContent);
    }

    @Test
    public void ParserDefineTable() {
      StringBuilder input = new StringBuilder();
      for (int i = 0; i < 30; i++) {
        input.append("!define x").append(i).append(" {|a|\n|b|}\n");
      }
      runParser2("define table2", input.toString());
      runParser3("define table3", input.toString());
    }

  private void runParser2(String name, String input) {
    long start = System.currentTimeMillis();
    WikiPage page = new TestRoot().makePage("NewTest");
    //String result = ParserTest.translateTo(new TestRoot().makePage("NewTest"), pageContent);
    SyntaxTreeV2 syntaxTree = new SyntaxTreeV2();
    syntaxTree.parse(input, new ParsingPage(new WikiSourcePage(page)));
    System.out.println(name + " parse " + (System.currentTimeMillis() - start));
    start = System.currentTimeMillis();
    /*String result =*/ syntaxTree.translateToHtml();
    System.out.println(name + " render " + (System.currentTimeMillis() - start));
    //System.out.println(result);
    assertEquals("done", "done");
  }

  private void runParser3(String name, String input) {
    long start = System.currentTimeMillis();
    WikiPage page = new TestRoot().makePage("NewTest");
    //String result = ParserTest.translateTo(new TestRoot().makePage("NewTest"), pageContent);
    SyntaxTree syntaxTree = new MarkUpSystemV3().parse(new ParsingPage(new WikiSourcePage(page)), input);
    System.out.println(name + " parse " + (System.currentTimeMillis() - start));
    start = System.currentTimeMillis();
    /*String result =*/ syntaxTree.translateToHtml();
    System.out.println(name + " render " + (System.currentTimeMillis() - start));
    //System.out.println(result);
    assertEquals("done", "done");
  }

    /** For dramatic effect, run in debug mode */
    @Test
    public void listDeserializationTest() {
      List<Object> objects = new ArrayList<>();
      for (int i = 0; i < 10000; i++) {
        objects.add("This is string " + i);
      }
      final String serializedList = SlimSerializer.serialize(objects);

      long start = System.currentTimeMillis();
      List<Object> result = SlimDeserializer.deserialize(serializedList);
      System.out.println("deserialize " + (System.currentTimeMillis() - start));

      assertEquals(objects, result);
    }

}
