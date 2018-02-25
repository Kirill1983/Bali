package org.kkonoplev.bali.suiteexec.resource;

public class RequiredTestResource {
	
	private Class<? extends TestExecResource> testExecResourceClass;
	private String properties;
	
	public RequiredTestResource(
			Class<? extends TestExecResource> testExecResourceClass,
			String properties) {
		super();
		this.testExecResourceClass = testExecResourceClass;
		this.properties = properties;
	}
	
	public Class<? extends TestExecResource> getTestExecResourceClass() {
		return testExecResourceClass;
	}
	public void setTestExecResourceClass(
			Class<? extends TestExecResource> testExecResourceClass) {
		this.testExecResourceClass = testExecResourceClass;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	
	
	public String toString(){
		return "ReqResource "+this.testExecResourceClass.getSimpleName()+" "+this.properties; 
	}
	

}
