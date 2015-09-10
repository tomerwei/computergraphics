package cgresearch.studentprojects.shapegrammar.generator;

import cgresearch.studentprojects.shapegrammar.datastructures.tree.RuleTree;
import cgresearch.studentprojects.shapegrammar.fileio.RuleReader;
import cgresearch.studentprojects.shapegrammar.settings.BuildingSettingManager;
import cgresearch.studentprojects.shapegrammar.settings.BuildingSettings;
import cgresearch.core.assets.ResourcesLocator;

public class BuildingGenerator {
  public void generateBuilding(String buildingDir, double width, double height,
      double length, double x, double z) {
    BuildingSettingManager.getInstance().createNewBuildingSettings();
    BuildingSettings buildingSettings =
        BuildingSettingManager.getInstance().getActualSettings();
    RuleReader ruleReader = new RuleReader();

    BuildingSettingManager.getInstance().startChanges();

    buildingSettings.setBuildingDir(buildingDir);
    buildingSettings.setWidth(width);
    buildingSettings.setHeight(height);
    buildingSettings.setLength(length);
    buildingSettings.setX(x);
    buildingSettings.setZ(z);

    RuleTree ruleTree =
        BuildingSettingManager.getInstance().getActualSettings().getRuleTree();
    String path =
        ResourcesLocator.getInstance().getPathToResource(
            BuildingSettingManager.getInstance().getBaseDirectory()
                + buildingSettings.getBuildingDir() + "/rules/start.rule");
    ruleTree.createRuleTree(ruleReader.readRulesFromFile(path));

    BuildingSettingManager.getInstance().notifyChanges();
  }
}
