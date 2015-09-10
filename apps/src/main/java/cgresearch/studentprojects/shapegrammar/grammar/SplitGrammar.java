package cgresearch.studentprojects.shapegrammar.grammar;

import java.util.ArrayList;
import java.util.List;

import cgresearch.studentprojects.shapegrammar.datastructures.tree.Node;
import cgresearch.studentprojects.shapegrammar.datastructures.tree.RuleTree;
import cgresearch.studentprojects.shapegrammar.datastructures.tree.VirtualFormTree;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.IVirtualForm;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualCuboid;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualMethod;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualPoint;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualShape;
import cgresearch.studentprojects.shapegrammar.settings.BuildingSettingManager;
import cgresearch.studentprojects.shapegrammar.util.IRulenameValuePair;
import cgresearch.studentprojects.shapegrammar.util.RandomRulenameValuePair;

/**
 * The Class SplitGrammar split the shapes to the lowest instance.
 * @author Thorben Watzl
 */
public class SplitGrammar {
	
	/** The rule tree. */
	private RuleTree ruleTree;
	
	/**
	 * Instantiates a new split grammar.
	 */
	public SplitGrammar(){
		ruleTree = BuildingSettingManager.getInstance().getActualSettings().getRuleTree();
	}
	
	/**
	 * Split.
	 *
	 * @param building the building
	 * @return the virtual form tree
	 */
	public VirtualFormTree split(IVirtualForm building, VirtualFormTree formTree) {
		formTree.addRootNode(building);
		split(formTree.getRootNode());
		return formTree;
	}
	
	/**
	 * Split.
	 *
	 * @param node the node
	 */
	private void split(Node<IVirtualForm> node){
		IVirtualForm form = node.getData();
		VirtualMethod splitMethod = ruleTree.getNodeByName(form.getName()).getData().getMethod();
		if(splitMethod != null){
			if(splitMethod.getName().equals("extrude")){
				form = new VirtualCuboid(form.getName(), form.getPosition(), form.getWidth(), form.getHeight(), Double.parseDouble(splitMethod.getParameters()));
				node.setData(form);
				VirtualCuboid cube = (VirtualCuboid)form;
				cuboidPerform(cube, node, splitMethod);
			}else if(splitMethod.getParameters().equals("f")){
				VirtualCuboid cube = (VirtualCuboid)form;
				cuboidPerform(cube, node, splitMethod);
			}else{
				VirtualShape shape = (VirtualShape)form;
				shapePerform(shape, node, splitMethod, splitMethod.getParameters());
			}
			
			List<Node<IVirtualForm>> childs = node.getChildren();
			for (Node<IVirtualForm> child : childs) {
				split(child);
			}
		}
	}
	
	/**
	 * Shape perform in given direction
	 * Split shapes in the given parts
	 * 
	 * @param shape the shape
	 * @param node the node
	 * @param splitMethod the split method
	 * @param direction the x or y direction
	 */
	private void shapePerform(VirtualShape shape, Node<IVirtualForm> node,
			VirtualMethod splitMethod,String direction){
		if(splitMethod.getName().equals("split")){
			List<IRulenameValuePair> ruleValuePairs = splitMethod.getDelegation();
			double rest = (direction.equals("x")?shape.getWidth():shape.getHeight());
			double position = 0;
			List<Node<IVirtualForm>> relativShapes = new ArrayList<Node<IVirtualForm>>();
			int iterator = 0;
			double tempRest = rest;
			while(tempRest > 0 && iterator <= ruleValuePairs.size()-1){
				IRulenameValuePair actualPair = ruleValuePairs.get(iterator);
				if(actualPair.getValues() != null){
					String value = actualPair.getValues().get(0);
					VirtualPoint virtualPos = (direction.equals("x")?new VirtualPoint(position, 0, 0):new VirtualPoint(0, position, 0));
					if(value.equals("~")){
						double width = (direction.equals("x")?0:shape.getWidth());
						double height = (direction.equals("x")?shape.getHeight():0);
						IVirtualForm childShape = new VirtualShape(actualPair.getRulename(), virtualPos, width, height);
						Node<IVirtualForm> childNode = new Node<IVirtualForm>();
						childNode.setData(childShape);
						node.addChild(childNode);
						relativShapes.add(childNode);
					}else{
						double childShapeSize = Double.parseDouble(value);
						tempRest = rest - childShapeSize;
						if(tempRest >= 0){
							double width = (direction.equals("x")?childShapeSize:shape.getWidth());
							double height = (direction.equals("x")?shape.getHeight():childShapeSize);
							IVirtualForm childShape = new VirtualShape(actualPair.getRulename(), virtualPos, width, height);
							position += childShapeSize;
							Node<IVirtualForm> childNode = new Node<IVirtualForm>();
							childNode.setData(childShape);
							node.addChild(childNode);
							rest = tempRest;
						}
					}
				}
				if(actualPair instanceof RandomRulenameValuePair){
					RandomRulenameValuePair rndPair = (RandomRulenameValuePair)actualPair;
					rndPair.resetSelected();
				}
				iterator++;
				if(iterator > ruleValuePairs.size()-1 && splitMethod.isRepeat()){
					iterator = 0;
				}
			}
			List<Node<IVirtualForm>> tempList = new ArrayList<Node<IVirtualForm>>();
			if(relativShapes.size() > 0 && rest > 0){
				tempList = relativShapes;
			}else if(relativShapes.size() == 0 && rest > 0){
				tempList = node.getChildren();
			}
			double relativSize = rest/tempList.size();
			VirtualPoint pos = null;
			for (Node<IVirtualForm> relativShape : tempList) {
				IVirtualForm childShape = relativShape.getData();
				childShape.setPosition(pos);
				if(direction.equals("x")){
					childShape.setWidth(relativSize+childShape.getWidth());
				}else{
					childShape.setHeight(relativSize+childShape.getHeight());
				}
			}
			for(Node<IVirtualForm> childNode : node.getChildren()){
				IVirtualForm childShape = childNode.getData();
				if(pos == null){
					pos = new VirtualPoint(0, 0, 0);
				}
				childShape.setPosition(pos);
				if(direction.equals("x")){
					pos = new VirtualPoint(pos.getX()+childShape.getWidth(), pos.getY(), pos.getZ(), pos.getDirection());
				}else{
					pos = new VirtualPoint(pos.getX(), pos.getY()+childShape.getHeight(), pos.getZ(), pos.getDirection());
				}
			}
		}
	}
	
	/**
	 * Cuboid perform.
	 * Split Cuboid in 5 Parts
	 *
	 * @param cuboid the Cuboid
	 * @param node the node
	 * @param splitMethod the split method
	 */
	private void cuboidPerform(VirtualCuboid cuboid, Node<IVirtualForm> node,
			VirtualMethod splitMethod) {
		List<IRulenameValuePair> ruleValuePairs = splitMethod.getDelegation();
		for (IRulenameValuePair ruleValuePair : ruleValuePairs) {
			String rule = ruleValuePair.getRulename();
			List<String> cubeSides = ruleValuePair.getValues();
			for (String side : cubeSides) {
				VirtualShape cubeSideShape = cuboid.getCuboidSide(side);
				VirtualShape shape = new VirtualShape(rule, cubeSideShape.getPosition(), cubeSideShape.getWidth(), cubeSideShape.getHeight());
				Node<IVirtualForm> childNode = new Node<IVirtualForm>();
				childNode.setData(shape);
				node.addChild(childNode);
			}
		}
	}

}
