package org.kkonoplev.bali.suiteexec;

import java.util.ArrayList;

import org.kkonoplev.bali.suiteexec.resource.TestExecResource;

public class LocalRunBaliContext {
	
	private String browserType;
	private String propertiesDir;
	private String resultDir;
	private String options;
	private boolean captureMode = false;
	private String localClass = "local.local";
	
	private ArrayList<TestExecResource> localResources = new ArrayList<TestExecResource>();

	
	public LocalRunBaliContext(){
	}
	
	
	public String getBrowserType() {
		return browserType;
	}
	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}
	public String getPropertiesDir() {
		return propertiesDir;
	}
	public void setPropertiesDir(String propertiesDir) {
		this.propertiesDir = propertiesDir;
	}
	public ArrayList<TestExecResource> getLocalResources() {
		return localResources;
	}
	public void setLocalResources(ArrayList<TestExecResource> localResources) {
		this.localResources = localResources;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public boolean isCaptureMode() {
		return captureMode;
	}
	public void setCaptureMode(boolean captureMode) {
		this.captureMode = captureMode;
	}
	public String getLocalClass() {
		return localClass;
	}
	public void setLocalClass(String localClass) {
		this.localClass = localClass;
	}
	public String getResultDir() {
		return resultDir;
	}
	public void setResultDir(String resultDir) {
		this.resultDir = resultDir;
	}
	

}
