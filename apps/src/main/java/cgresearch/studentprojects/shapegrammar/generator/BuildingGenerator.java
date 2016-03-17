package cgresearch.studentprojects.shapegrammar.generator;

import cgresearch.studentprojects.shapegrammar.datastructures.tree.RuleTree;
import cgresearch.studentprojects.shapegrammar.fileio.RuleReader;
import cgresearch.studentprojects.shapegrammar.settings.BuildingSettings;
import cgresearch.studentprojects.shapegrammar.visualize.BuildingVisualizer;

import java.util.List;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.scenegraph.CgNode;

public class BuildingGenerator {

	public CgNode generateBuildingCgNode(String buildingDir, double width, double height, double length, double x,
			double z) {
		CgNode buildingNode = new CgNode(null, "Building " + buildingDir);

		// Rule erzeugen
		BuildingSettings settings = generateBuilding(buildingDir, width, height, length, x, z);

		// Meshes bauen aus Rule
		BuildingVisualizer visualizer = new BuildingVisualizer();
		List<ITriangleMesh> meshes = visualizer.generateBuilding(settings);
		for (ITriangleMesh mesh : meshes) {
			CgNode meshNode = new CgNode(mesh, "Wall");
			buildingNode.addChild(meshNode);
		}

		Logger.getInstance().message("Building Vizualisiert");
		return buildingNode;
	}

	private BuildingSettings generateBuilding(String buildingDir, double width, double height, double length, double x,
			double z) {
		BuildingSettings buildingSettings = new BuildingSettings();
		RuleReader ruleReader = new RuleReader();

		buildingSettings.setBuildingDir(buildingDir);
		buildingSettings.setWidth(width);
		buildingSettings.setHeight(height);
		buildingSettings.setLength(length);
		buildingSettings.setX(x);
		buildingSettings.setZ(z);

		RuleTree ruleTree = buildingSettings.getRuleTree();
		String path = ResourcesLocator.getInstance().getPathToResource(
				buildingSettings.getBaseDirectory() + buildingSettings.getBuildingDir() + "/rules/start.rule");
		ruleTree.createRuleTree(ruleReader.readRulesFromFile(path, buildingSettings));

		return buildingSettings;
	}
}
