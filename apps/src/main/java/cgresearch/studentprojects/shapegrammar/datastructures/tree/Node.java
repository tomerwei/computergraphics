package cgresearch.studentprojects.shapegrammar.datastructures.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Generic Node Class
 * 
 * @author Thorben Watzl
 * @param <T> Type Of The Node Data
 */
public class Node<T> {
	private T data;
	private Node<T> parent;
    private List<Node<T>> children;
    
    /**
	 * Default Constructor For The GrammarRule Class
	 */
    public Node(){
    	children = new ArrayList<Node<T>>();
    }
    
    /**
	 * Return The Node Date
	 * @return Node Data From Type T
	 */
    public T getData() {
		return data;
	}
    
    /**
	 * Set The Data From Type T
	 * @param data Set The Data From The Node
	 */
	public void setData(T data) {
		this.data = data;
	}
	
	/**
	 * Return The Parent Node
	 * @return Parent Node
	 */
	public Node<T> getParent() {
		return parent;
	}
	
	/**
	 * Set The Parent Node
	 * @param parent Parent Node
	 */
	public void setParent(Node<T> parent) {
		this.parent = parent;
	}
	
	/**
	 * Return A List Of All Child Nodes
	 * @return List Of All Child Nodes
	 */
	public List<Node<T>> getChildren() {
		return children;
	}
	
	/**
	 * Set The Child Node List
	 * @param children New Child Node List
	 */
	public void setChildren(List<Node<T>> children) {
		this.children = children;
	}
	
	/**
	 * Add A Child To The Node
	 * @param child Child Node
	 */
	public void addChild(Node<T> child) {
		child.setParent(this);
		this.children.add(child);
	}
}
