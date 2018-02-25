package org.kkonoplev.bali.classifyreport.model.artifact;


public class WarningCaseIMGArtifact extends WarningCaseResultDirArtifact {
	
	private String imglink;
	
	public WarningCaseIMGArtifact(String imglink){
		this.imglink = imglink;
	}
	
	public String toJSON(){
		return "{type:'IMG', img:'"+getPreUrl()+imglink+"'}";
	}

	
}
