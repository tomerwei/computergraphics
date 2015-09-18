package cgresearch.studentprojects.shapegrammar;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.ConsoleLogger;
import cgresearch.core.logging.Logger.VerboseMode;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.studentprojects.shapegrammar.gui.application.GrammarTreeView;
import cgresearch.studentprojects.shapegrammar.gui.menu.BuilderMenu;
import cgresearch.studentprojects.shapegrammar.visualize.BuildingVisualizer;
import cgresearch.studentprojects.shapegrammar.visualize.CityVisualizer;

/**
 * Initial frame for the Shape Grammar project (Watzl)
 *
 */
public class ShapeGrammarFrame extends CgApplication {
  /**
   * Constructor
   */
  public ShapeGrammarFrame() {
  }

  public static void main(String[] args) {
    new ConsoleLogger(VerboseMode.NORMAL);
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    ShapeGrammarFrame app = new ShapeGrammarFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    GrammarTreeView grammarTreeView = new GrammarTreeView();
    BuildingVisualizer buildingVisualizer = new BuildingVisualizer();
    buildingVisualizer.setCgRoot(app.getCgRootNode());
    CityVisualizer cityVisualizer = new CityVisualizer();
    cityVisualizer.start();
    appLauncher.addCustomMenu(new BuilderMenu());
    appLauncher.addCustomUi(grammarTreeView);
  }

}
