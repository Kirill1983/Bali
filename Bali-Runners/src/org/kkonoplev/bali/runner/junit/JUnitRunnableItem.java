package org.kkonoplev.bali.runner.junit;

import java.util.List;

import org.kkonoplev.bali.runner.ProjectRunner;
import org.kkonoplev.bali.runner.RunnableItem;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;

public class JUnitRunnableItem implements RunnableItem {
	
	Class testClass;
	JUnitProject project;	


	private List<RequiredTestResource> requiredTestResources;
	
	public JUnitRunnableItem(){		
	}
	
	
	public JUnitProject getProject() {
		return project;
	}


	public void setProject(JUnitProject project) {
		this.project = project;
	}


	public void setTestClass(Class testClass) {
		this.testClass = testClass;
	}

	public Class getTestClass() {
		return testClass;
	}


	@Override
	public List<RequiredTestResource> getRequiredTestResources() {
		return requiredTestResources;
	}
	
	public void setRequiredTestResources(List<RequiredTestResource> requiredTestResources) {
		this.requiredTestResources = requiredTestResources;
	}


	@Override
	public Class<? extends ProjectRunner> getRunnerClass() {
		return JUnitProjectRunner.class;
	}
	
}
