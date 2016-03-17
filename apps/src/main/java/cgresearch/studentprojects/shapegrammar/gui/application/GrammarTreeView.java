package cgresearch.studentprojects.shapegrammar.gui.application;

import java.awt.GridLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import cgresearch.ui.IApplicationControllerGui;
import cgresearch.studentprojects.shapegrammar.datastructures.rules.GrammarRule;
import cgresearch.studentprojects.shapegrammar.datastructures.tree.Node;
import cgresearch.studentprojects.shapegrammar.datastructures.tree.RuleTree;

/**
 * The Class GrammarTreeView is the View to show the Grammar Tree.
 * 
 * @author Thorben Watzl
 */
public class GrammarTreeView extends IApplicationControllerGui implements Observer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2968489314641676925L;

	/** The tree view. */
	private JTree treeView;

	/**
	 * Instantiates a new grammar tree view.
	 */
	public GrammarTreeView() {
		setLayout(new GridLayout(0, 1));
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.gui.IApplicationControllerGui#getName()
	 */
	@Override
	public String getName() {
		return "Grammar Tree";
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {

		if (o instanceof RuleTree) {
			RuleTree ruleTree = (RuleTree) o;

			Node<GrammarRule> root = ruleTree.getRootNode();
			DefaultMutableTreeNode tNode = new DefaultMutableTreeNode(root.getData().getName());
			drawGrammarTree(root, tNode);
			if (treeView != null) {
				remove(treeView);
			}
			treeView = new JTree(tNode);
			add(treeView);
		}
	}

	/**
	 * Draw grammar tree.
	 *
	 * @param node
	 *            the node
	 * @param tNode
	 *            the t node
	 */
	private void drawGrammarTree(Node<GrammarRule> node, DefaultMutableTreeNode tNode) {
		List<Node<GrammarRule>> childs = node.getChildren();
		if (!childs.isEmpty()) {
			for (Node<GrammarRule> childNode : childs) {
				DefaultMutableTreeNode childTNode = new DefaultMutableTreeNode(childNode.getData().getName());
				tNode.add(childTNode);
				drawGrammarTree(childNode, childTNode);
			}
		}
	}
}
