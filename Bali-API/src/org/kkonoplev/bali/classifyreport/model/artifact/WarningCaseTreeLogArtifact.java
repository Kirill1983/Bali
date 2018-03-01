package org.kkonoplev.bali.classifyreport.model.artifact;


public class WarningCaseTreeLogArtifact extends WarningCaseResultDirArtifact {
	
	private String threadId;
	private String id;
	
	public WarningCaseTreeLogArtifact(String id, String threadId){
		this.threadId = threadId;
		this.id = id;
		setPreUrl("/bali/");
	}

	public String toJSON(){
		return "{type:'TreeLog', id:'"+id+"', threadId:'"+threadId+"', nodeUrl:'"+getPreUrl()+"'}";
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}
