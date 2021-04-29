package org.kkonoplev.bali.runner.junit;

import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.runner.ProjectAdapter;
import org.kkonoplev.bali.runner.ProjectBuilder;

public class JUnitProjectAdapter implements ProjectAdapter {

	private JUnitProjectBuilder projectBuilder;
		
	public JUnitProjectAdapter(){
		projectBuilder = new JUnitProjectBuilder();
	}

	@Override
	public ProjectBuilder getProjectBuilder() {
		return projectBuilder;
	}


	@Override
	public BaseProject buildEmptyProject() {
		return new JUnitProject();
	}
	
	

}
