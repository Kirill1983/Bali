package org.kkonoplev.bali.treelogger;

import java.io.Serializable;
import java.util.ArrayList;

public class LogNode implements Serializable {
	
	private String text = "";
	private String title = "";
	private String id = "";
	private ArrayList<LogNode> childs = new ArrayList<LogNode>();
	private LogNode parent;
	private boolean failed = false;
	
	public enum State {
		CHECKED("checked"),
		UNDERTERMINED("undetermined"),
		UNCHECKED("");
		
		String name;
		
		State(String name_){
			name = name_;
		}
		
		public String getName(){
			return name;
		}
	};
	
	public LogNode(String text) {
		this.text = text;
	}
	
	public LogNode(String text, boolean failed) {
		this.text = text;
		this.failed = failed;
	}
	
	public LogNode(String text, boolean failed, String id) {
		this.text = text;
		this.failed = failed;
		this.id = id;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public ArrayList<LogNode> getChilds() {
		return childs;
	}
	public void setChilds(ArrayList<LogNode> childs) {
		this.childs = childs;
	}
	public LogNode getParent() {
		return parent;
	}
	public void setParent(LogNode parent) {
		this.parent = parent;
	}
	public void addChild(LogNode node) {
		node.setParent(this);
		getChilds().add(node);
	}
	
	public boolean isLeaf(){
		return getChilds().size() == 0 ? true : false;
	}

	public boolean isFolder(){
		return !isLeaf();
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
