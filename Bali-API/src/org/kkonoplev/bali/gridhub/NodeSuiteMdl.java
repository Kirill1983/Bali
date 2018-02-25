package org.kkonoplev.bali.gridhub;

import java.io.Serializable;

import org.kkonoplev.bali.suiteexec.SuiteMdl;

public class NodeSuiteMdl implements Serializable {

	private SuiteMdl suiteMdl;
	private GridNode node;
	
	public NodeSuiteMdl(SuiteMdl suiteMdl, GridNode node) {
		super();
		this.suiteMdl = suiteMdl;
		this.node = node;
	}

	public SuiteMdl getSuiteMdl() {
		return suiteMdl;
	}

	public void setSuiteMdl(SuiteMdl suiteMdl) {
		this.suiteMdl = suiteMdl;
	}

	public GridNode getNode() {
		return node;
	}

	public void setNode(GridNode node) {
		this.node = node;
	}

	public String toString(){
		return "Node Id: "+node.getId()+", "+suiteMdl.toStringFull();
	}
	
}
