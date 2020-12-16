package fitnesse.testsystems.slim;

import fitnesse.plugins.PluginFeatureFactoryBase;
import fitnesse.slim.test.MyFixture;
import fitnesse.testsystems.slim.tables.SlimTableFactory;
import fitnesse.wikitext.ParsingPage;
import fitnesse.wikitext.parser.SymbolProvider;
import fitnesse.wikitext.parser.Table;
import fitnesse.wikitext.shared.MarkUpConfig;
import fitnesse.wikitext.shared.Names;
import fitnesse.wikitext.shared.SyntaxNode;

import java.util.logging.Logger;

/**
 * Test plugin used in Acceptance tests suite showing how custom symbol rendering can be plugged in.
 */
public class CustomScriptTableRenderingPlugin extends PluginFeatureFactoryBase {

  private static final Logger LOG = Logger.getLogger(CustomScriptTableRenderingPlugin.class.getName());

  @Override
  public void registerSymbolTypes(SymbolProvider symbolProvider) {
    TableSymbolDecorator.install();
  }

  @Override
  public void registerSlimTables(SlimTableFactory slimTableFactory) {
    LOG.info("Creating alias from \"my use case\" to \"script:" + MyFixture.class.getSimpleName() + "\"");
    slimTableFactory.addAlias("my use case", MyFixture.class.getSimpleName());
  }

  private static class TableSymbolDecorator {
    static void install() {
      MarkUpConfig.addDecorator(Table.symbolType.toString(), TableSymbolDecorator::handleParsedSymbol);
      //Table.symbolType.addDecorator(new TableSymbolDecorator());
    }

    static void handleParsedSymbol(SyntaxNode table, ParsingPage page) {
      SyntaxNode firstCell = table.getChildren()
                              .get(0)
                              .getChildren()
                              .get(0);
      String firstCellContent = firstCell.getAllContent();
      if (firstCellContent.contains("script") && firstCellContent.contains("my use case")) {
        table.appendProperty(Names.CLASS, "myUseCase");
      }
    }
  }
}
