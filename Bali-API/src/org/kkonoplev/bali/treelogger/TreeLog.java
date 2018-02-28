package org.kkonoplev.bali.treelogger;

import java.io.Serializable;

public class TreeLog implements Serializable {
	
	private LogNode root = new LogNode("root");
	private LogNode current = root;
	private String name = "treelog";
	
	public TreeLog(){
	}
	
	public TreeLog(String name) {
		super();
		this.name = name;
	}

	public LogNode getRoot() {
		return root;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public void openLevel(String msg) {
		LogNode node = new LogNode(msg);
		current.addChild(node);
		current = node;
	}

	public void closeLevel() {
		current = current.getParent();
	}

	public void log(String msg) {
		current.addChild(new LogNode(msg));
	}

	public void logFail(String msg) {
		current.addChild(new LogNode(msg, true));
	}
	
	public void addFailMarkToLastMsg(){
		
		if (current.isFolder()){
			LogNode last = current.getChilds().get(current.getChilds().size()-1);
			last.setFailed(true);
		} else 
			current.setFailed(true);
		
	}

}
