package org.kkonoplev.bali.gridhub;

import java.io.Serializable;

public class GridNode implements Serializable {

	private String id;
	private String url;
	
	
	public GridNode(String id, String url) {
		super();
		this.id = id;
		this.url = url;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
