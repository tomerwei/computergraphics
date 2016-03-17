package cgresearch.studentprojects.shapegrammar.grammar;

import cgresearch.studentprojects.shapegrammar.datastructures.tree.RuleTree;
import cgresearch.studentprojects.shapegrammar.datastructures.tree.VirtualFormTree;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualCuboid;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualPoint;
import cgresearch.studentprojects.shapegrammar.settings.BuildingSettings;

/**
 * The Class ShapeGrammar is the main Grammar instance and use the splitgrammar
 * and attributegrammar.
 * 
 * @author Thorben Watzl
 */
public class ShapeGrammar {

	/**
	 * Perform grammar.
	 *
	 * @return the virtual form tree
	 */
	public VirtualFormTree performGrammar(BuildingSettings buildingSettings) {
		RuleTree ruleTree = buildingSettings.getRuleTree();
		VirtualCuboid building = new VirtualCuboid(ruleTree.getRootNode().getData().getName(),
				new VirtualPoint(buildingSettings.getX(), 0, buildingSettings.getZ()), buildingSettings.getWidth(),
				buildingSettings.getHeight(), buildingSettings.getLength());
		SplitGrammar splitGrammar = new SplitGrammar();
		AttributeGrammar attributeGrammar = new AttributeGrammar();
		VirtualFormTree formTree = splitGrammar.split(building, buildingSettings);
		attributeGrammar.setAttributes(formTree, buildingSettings);
		return formTree;
	}
}
