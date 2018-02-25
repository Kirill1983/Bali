package org.kkonoplev.bali.gridhub;

import java.util.ArrayList;

public class GridHub {

	private ArrayList<GridNode> nodes = new ArrayList<GridNode>();

	public ArrayList<GridNode> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<GridNode> nodes) {
		this.nodes = nodes;
	}

	public GridNode getNode(int i) {
		return nodes.get(i);
	}

	public GridNode getNodeById(String nodeId) {
		for (GridNode node : nodes)
			if (node.getId().equals(nodeId))
				return node;
		
		return null;
	}


}
