package org.kkonoplev.bali.classifyreport.model;

import java.io.Serializable;

public class OnNodeSuite implements Serializable {

	private int id = 0;
	private String nodeUrl = "";
	private String resultDir = "";
	
	public OnNodeSuite(int id, String nodeUrl, String resultDir) {
		super();
		this.id = id;
		this.nodeUrl = nodeUrl;
		this.resultDir = resultDir;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNodeUrl() {
		return nodeUrl;
	}
	public void setNodeUrl(String nodeUrl) {
		this.nodeUrl = nodeUrl;
	}
	public String getResultDir() {
		return resultDir;
	}
	public void setResultDir(String resultDir) {
		this.resultDir = resultDir;
	}
	
	
}
