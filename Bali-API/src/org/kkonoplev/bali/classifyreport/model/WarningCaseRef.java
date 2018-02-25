package org.kkonoplev.bali.classifyreport.model;

public class WarningCaseRef {
	
	private String warningId = "";
	private String warningCaseId = "";

	public WarningCaseRef(String warningId, String warningCaseId) {
		super();
		this.warningId = warningId;
		this.warningCaseId = warningCaseId;
	}

	public String getWarningId() {
		return warningId;
	}

	public void setWarningId(String warningId) {
		this.warningId = warningId;
	}

	public String getWarningCaseId() {
		return warningCaseId;
	}

	public void setWarningCaseId(String warningCaseId) {
		this.warningCaseId = warningCaseId;
	}
	

}
