package org.kkonoplev.bali.runner.main;

import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.runner.ProjectAdapter;
import org.kkonoplev.bali.runner.ProjectBuilder;

public class MainProjectAdapter implements ProjectAdapter {

	private MainProjectBuilder projectBuilder;
	
	public MainProjectAdapter(){
		projectBuilder = new MainProjectBuilder();
	}
	
	@Override
	public ProjectBuilder getProjectBuilder() {
		return projectBuilder;
	}


	@Override
	public BaseProject buildEmptyProject() {
		return new MainProject();
	}
	
	

}
