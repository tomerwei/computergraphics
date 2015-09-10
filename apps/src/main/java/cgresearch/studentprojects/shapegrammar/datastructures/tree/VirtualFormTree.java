package cgresearch.studentprojects.shapegrammar.datastructures.tree;

import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.IVirtualForm;


/**
 * The VirtualFormTree Contains IVirtualForms. These can be VirtualShapes or VirtualCubes
 * @author Thor
 */
public class VirtualFormTree extends SimpleTree<IVirtualForm> {
	
	/**
	 * Add Root Node
	 * @param rootNode The Root Node
	 */
	public void addRootNode(IVirtualForm rootNode){
		crateRootNode(rootNode);
	}
	
	/**
	 * Return Node By Given Name
	 * @param name Given Name
	 * @return Node With Given Name
	 */
	public Node<IVirtualForm> getNodeByName(String name){
		return getNameFromTree(name,getRootNode());
	}
	
	/**
	 * Return The Node By Given Name Recursive
	 * @param name Given Name
	 * @param node Node Will Checked For Given Name
	 * @return Return The Node With Given Name
	 */
	private Node<IVirtualForm> getNameFromTree(String name, Node<IVirtualForm> node) {
		if(!node.getData().getName().equals(name)){
			for (Node<IVirtualForm> child : node.getChildren()) {
				Node<IVirtualForm> returnVal = getNameFromTree(name, child);
				if(returnVal != null){
					return returnVal;
				}
			}
			return null;
		}else{
			return node;
		}
	}
}
