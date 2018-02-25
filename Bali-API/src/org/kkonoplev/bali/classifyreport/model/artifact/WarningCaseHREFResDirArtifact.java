package org.kkonoplev.bali.classifyreport.model.artifact;


public class WarningCaseHREFResDirArtifact extends WarningCaseResultDirArtifact {
	
	private String href;
	private String name;
	
	public WarningCaseHREFResDirArtifact(String name, String href){
		this.href = href;
		this.name = name;
	}

	public String toJSON(){
		return "{type:'HREF', href:'"+getPreUrl()+href+"', name:'"+name+"'}";
	}
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	

}
