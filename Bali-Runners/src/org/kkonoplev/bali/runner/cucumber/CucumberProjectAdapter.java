package org.kkonoplev.bali.runner.cucumber;

import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.runner.ProjectAdapter;
import org.kkonoplev.bali.runner.ProjectBuilder;

public class CucumberProjectAdapter implements ProjectAdapter {

	private CucumberProjectBuilder projectBuilder;
	
	public CucumberProjectAdapter(){
		projectBuilder = new CucumberProjectBuilder();
	}

	
	@Override
	public ProjectBuilder getProjectBuilder() {
		return projectBuilder;
	}

	
	@Override
	public BaseProject buildEmptyProject() {
		return new CucumberProject();
	}
	
	

}
