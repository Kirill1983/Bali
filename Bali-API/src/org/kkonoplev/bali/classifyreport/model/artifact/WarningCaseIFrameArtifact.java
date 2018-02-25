package org.kkonoplev.bali.classifyreport.model.artifact;


public class WarningCaseIFrameArtifact extends WarningCaseResultDirArtifact {

	private String file;

	public WarningCaseIFrameArtifact(String file){
		this.file = file;
	}
	
	public String toJSON(){
		return "{type:'IFrameView', href:'"+getPreUrl()+file+"'}";
	}
	
	public String getFile() {
		return file;
	}
	
	public void setFile(String file) {
		this.file = file;
	}

}
