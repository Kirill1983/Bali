package org.kkonoplev.bali.runner.main;

import java.util.List;

import org.kkonoplev.bali.runner.ProjectRunner;
import org.kkonoplev.bali.runner.RunnableItem;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;

public class MainRunnableItem implements RunnableItem {
	
	Class testClass;
	MainProject project;

	private List<RequiredTestResource> requiredTestResources;
	
	public MainRunnableItem(){		
	}
	
	public void setTestClass(Class testClass) {
		this.testClass = testClass;
	}

	public Class getTestClass() {
		return testClass;
	}

	public MainProject getProject() {
		return project;
	}

	public void setProject(MainProject project) {
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
		return MainProjectRunner.class;
	}
	
}
