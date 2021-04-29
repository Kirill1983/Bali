package org.kkonoplev.bali.runner.cucumber;

import java.util.ArrayList;
import java.util.List;

import org.kkonoplev.bali.runner.ProjectRunner;
import org.kkonoplev.bali.runner.RunnableItem;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;

public class CucumberRunnableItem implements RunnableItem {

	String scenarioName = "";
	String featureFile = "";
	String stepsDir = "";
	
	private CucumberProject cucumberProject;	
	private List<RequiredTestResource> requiredTestResources = new ArrayList<RequiredTestResource>();
	
	public CucumberRunnableItem(){
	}
	
	public CucumberRunnableItem(String scenarioName, String featureFile,
			String stepsDir, CucumberProject cucumberProject) {
		super();
		this.scenarioName = scenarioName;
		this.featureFile = featureFile;
		this.stepsDir = stepsDir;
		this.cucumberProject = cucumberProject;
	}

	public String getScenarioName() {
		return scenarioName;
	}
	
	public String getFeatureFile() {
		return featureFile;
	}
	
	public String getStepsDir() {
		return stepsDir;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public void setFeatureFile(String featureFile) {
		this.featureFile = featureFile;
	}

	public void setStepsDir(String stepsDir) {
		this.stepsDir = stepsDir;
	}

	public CucumberProject getCucumberProject() {
		return cucumberProject;
	}

	public void setCucumberProject(CucumberProject cucumberProject) {
		this.cucumberProject = cucumberProject;
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
		return CucumberProjectRunner.class;
	}
		
}
