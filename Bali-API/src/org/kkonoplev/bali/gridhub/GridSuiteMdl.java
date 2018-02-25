package org.kkonoplev.bali.gridhub;

import java.io.Serializable;
import java.util.ArrayList;

public class GridSuiteMdl implements Serializable {

	ArrayList<NodeSuiteMdl> nodeSuites = new ArrayList<NodeSuiteMdl>();

	public ArrayList<NodeSuiteMdl> getNodeSuites() {
		return nodeSuites;
	}

	public void setNodeSuites(ArrayList<NodeSuiteMdl> nodeSuites) {
		this.nodeSuites = nodeSuites;
	}

	
}
