package org.kkonoplev.bali.runner.testng;

import java.util.List;

import org.kkonoplev.bali.runner.ProjectRunner;
import org.kkonoplev.bali.runner.RunnableItem;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;

public class TestNGRunnableItem implements RunnableItem {
	
	Class testClass;
	TestNGProject project; 
	
	private List<RequiredTestResource> requiredTestResources;
	
	public TestNGRunnableItem(){		
	}
	
	public void setTestClass(Class testClass) {
		this.testClass = testClass;
	}

	public Class getTestClass() {
		return testClass;
	}

	public TestNGProject getProject() {
		return project;
	}

	public void setProject(TestNGProject project) {
		this.project = project;
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
		return TestNGProjectRunner.class;
	}
	
}
