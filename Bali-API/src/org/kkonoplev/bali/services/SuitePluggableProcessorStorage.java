package org.kkonoplev.bali.services;

public class SuitePluggableProcessorStorage {
	
	private String className = "";
	private String defaultmeta = "";
	private SuitePluggableProcessor suitePluggableProcessor;
	
	public SuitePluggableProcessorStorage(){
	}
		
	public boolean isEmpty(){
		return className.equals(""); 
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getDefaultmeta() {
		return defaultmeta;
	}
	
	public void setDefaultmeta(String defaultmeta) {
		this.defaultmeta = defaultmeta;
	}

	public SuitePluggableProcessor getSuitePluggableProcessor() {
		return suitePluggableProcessor;
	}

	public void setSuitePluggableProcessor(
			SuitePluggableProcessor suitePluggableProcessor) {
		this.suitePluggableProcessor = suitePluggableProcessor;
	}

}
