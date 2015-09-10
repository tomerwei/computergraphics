package cgresearch.studentprojects.shapegrammar.datastructures.tree;

/**
 * Simple Tree Implementation
 * 
 * @author Thorben Watzl
 * @param <T> Type Of The Node Data
 */
public class SimpleTree<T>{
	/**
	 * The Root Node
	 */
	private Node<T> root;
	
	/**
	 * Create The Root Node With The Given Data
	 * @param rootData The Root Data
	 */
	public void crateRootNode(T rootData){
		root = new Node<T>();
		root.setData(rootData);
		root.setParent(null);
	}
	
	/**
	 * Return The Root Node
	 * @return Root Node
	 */
	public Node<T> getRootNode(){
		return root;
	}
}
