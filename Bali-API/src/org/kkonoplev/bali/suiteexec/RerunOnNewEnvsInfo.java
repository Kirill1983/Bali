package org.kkonoplev.bali.suiteexec;

public class RerunOnNewEnvsInfo {
	
	private String resultDir;
	private String[] environments;
	private String warningIDs;
	
	public RerunOnNewEnvsInfo(){
	}
	
	public RerunOnNewEnvsInfo(String resultDir, String environments[],
			String warningIDs) {
		super();
		this.resultDir = resultDir;
		this.environments = environments;
		this.warningIDs = warningIDs;
	}
	
	public String getResultDir() {
		return resultDir;
	}
	public void setResultDir(String resultDir) {
		this.resultDir = resultDir;
	}
	public String[] getEnvironments() {
		return environments;
	}
	public void setEnvironments(String[] environments) {
		this.environments = environments;
	}
	public String getWarningIDs() {
		return warningIDs;
	}
	public void setWarningIDs(String warningIDs) {
		this.warningIDs = warningIDs;
	}
	
	public String toString(){
		return "Dir: "+resultDir+", warningIDs="+warningIDs+", envs count="+environments.length;
	}
	

}
