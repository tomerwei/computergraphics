package cgresearch.studentprojects.shapegrammar.datastructures.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cgresearch.studentprojects.shapegrammar.datastructures.rules.GrammarRule;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualMethod;
import cgresearch.studentprojects.shapegrammar.util.IRulenameValuePair;
import cgresearch.studentprojects.shapegrammar.util.RandomRulenameValuePair;

/**
 * The RuleTree Contain The Nodes Witch Contains The RuleGrammar
 * 
 * @author Thorben Watzl
 *
 */
public class RuleTree extends SimpleTree<GrammarRule> {
	private Map<String, Node<GrammarRule>> nodeMap;

	/**
	 * Create The RuleTree From A Map Of String As Key and GrammarRule As Value
	 * 
	 * @param rootDataList
	 */
	public void createRuleTree(Map<String, GrammarRule> rootDataList) {
		crateRootNode(rootDataList.get("Building"));
		nodeMap = new HashMap<String, Node<GrammarRule>>();
		Node<GrammarRule> root = getRootNode();
		nodeMap.put(root.getData().getName(), root);
		createTree(rootDataList, getRootNode());
	}

	/**
	 * Create The Tree
	 * 
	 * @param rootDataList
	 *            Map of String GrammarRule Pairs
	 * @param node
	 *            The Actual Node
	 */
	private void createTree(Map<String, GrammarRule> rootDataList, Node<GrammarRule> node) {
		GrammarRule rule = node.getData();
		VirtualMethod vMethod = rule.getMethod();
		if (vMethod != null) {
			List<IRulenameValuePair> delegations = rule.getMethod().getDelegation();
			for (IRulenameValuePair delegation : delegations) {
				if (delegation instanceof RandomRulenameValuePair) {
					RandomRulenameValuePair randDelegation = (RandomRulenameValuePair) delegation;
					for (IRulenameValuePair randDelegItem : randDelegation.getAllElements()) {
						addNodeToRuleTree(node, randDelegItem, rootDataList);
					}
				} else {
					addNodeToRuleTree(node, delegation, rootDataList);
				}
			}
		}
	}

	private void addNodeToRuleTree(Node<GrammarRule> node, IRulenameValuePair ruleValuePair,
			Map<String, GrammarRule> rootDataList) {
		String rulename = ruleValuePair.getRulename();
		if (!nodeMap.containsKey(rulename)) {
			Node<GrammarRule> nextNode = new Node<GrammarRule>();
			nextNode.setData(rootDataList.get(rulename));
			nextNode.setParent(node);
			node.addChild(nextNode);
			nodeMap.put(rulename, nextNode);
			createTree(rootDataList, nextNode);
		} else {
			node.addChild(nodeMap.get(rulename));
		}
	}

	/**
	 * Return A Node From The Given Name
	 * 
	 * @param name
	 *            Name Of The Node
	 * @return Node With The Given Name
	 */
	public Node<GrammarRule> getNodeByName(String name) {
		return nodeMap.get(name);
	}
}
