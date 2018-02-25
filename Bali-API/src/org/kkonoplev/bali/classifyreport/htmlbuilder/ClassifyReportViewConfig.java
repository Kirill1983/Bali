package org.kkonoplev.bali.classifyreport.htmlbuilder;

public enum ClassifyReportViewConfig {
	
	DEFAULT("Error Classify Report", "Reason", "Error Message", "Test", true),
	CAPTURE("Test Flow Capture Report", "Action",  "Test", "Steps", false);
	
	ClassifyReportViewConfig(String headerText, String column1Text, String column2Text, String column3Text, boolean rerunBlockShow){
		
		this.headerText = headerText;
		this.column1Text = column1Text;
		this.column2Text = column2Text;
		this.column3Text = column3Text;
		this.rerunBlockShow = rerunBlockShow;
		
	}
	
	public String getHeaderText() {
		return headerText;
	}
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
	public String getColumn1Text() {
		return column1Text;
	}
	public void setColumn1Text(String column1Text) {
		this.column1Text = column1Text;
	}
	public String getColumn2Text() {
		return column2Text;
	}
	public void setColumn2Text(String column2Text) {
		this.column2Text = column2Text;
	}
	public String getColumn3Text() {
		return column3Text;
	}
	public void setColumn3Text(String column3Text) {
		this.column3Text = column3Text;
	}

	
	public boolean isRerunBlockShow() {
		return rerunBlockShow;
	}

	public void setRerunBlockShow(boolean rerunBlockShow) {
		this.rerunBlockShow = rerunBlockShow;
	}


	private boolean rerunBlockShow = true;
	private String headerText = "";
	private String column1Text = "";
	private String column2Text = "";
	private String column3Text = "";
	
}
