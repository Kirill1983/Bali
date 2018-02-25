package org.kkonoplev.bali.gridhub;

import java.util.ArrayList;

import org.kkonoplev.bali.project.structure.TreeNode;

public class TreeBuilder {

	public TreeNode build(ArrayList<String> leafs){

		TreeNode root = null;
		for (String leaf: leafs){
			
			TreeNode branch = buildTreePath(leaf);
			
			if (root == null)
				root = branch;
			else
				merge(root, branch);
			
		}
			
		return root;
	}

	private void merge(TreeNode root, TreeNode branch) {
		TreeNode branchNode = branch;
		TreeNode rootNode = root;
		
		while (rootNode.getName().equals(branchNode.getName())){
			
			branchNode = branchNode.getChilds().get(0);
			TreeNode sameChild = findWithNodeName(rootNode, branchNode.getName());
			if (sameChild == null){
				rootNode.getChilds().add(branchNode);
				return;
			} else {
				rootNode = sameChild;
			}
				
		}
		
	}

	private TreeNode findWithNodeName(TreeNode rootNode, String name) {
		for (TreeNode node: rootNode.getChilds()){
			if (node.getName().equals(name))
				return node;
		}
		return null;
	}

	private TreeNode buildTreePath(String leaf) {

		String[] path = leaf.split("\\.");
		
		TreeNode rootNode = new TreeNode();
		rootNode.setName("root");
		
		TreeNode lastRoot = rootNode;
		for (int i = 0; i < path.length; i++){
			
			TreeNode node = new TreeNode();
			node.setName(path[i]);
			node.setPath(path[i]);
			node.setLabel(path[i]);
			node.setTitle(path[i]);
			node.setPath(leaf);
			
			
			lastRoot.getChilds().add(node);
			lastRoot = node;
			
		}
		
		return rootNode;
		
	}
	// com.test.ab - > com, test, ab
	// com.test.bc
	// com.test.ca
//	private TreeNode buildTreePath(String[] path, int i) {
//		if (i >= path.length)
//			return null;
//		
//		TreeNode treeNode = new TreeNode();
//		
//		
//			
//	}
}
