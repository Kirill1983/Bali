package org.kkonoplev.bali.classifyreport.model.artifact;


public class WarningCaseResultDirArtifact extends WarningCaseArtifact {
	
	protected String preUrl = "";
	
	public String getPreUrl() {
		return preUrl;
	}

	public WarningCaseResultDirArtifact(){
	}
	
	public void setPreUrl(String str){
		this.preUrl = str;
	}
	
	public  String toJSON(){
		return "{}";
	}

}
