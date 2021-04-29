package org.kkonoplev.bali.runner.testng;

import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.runner.ProjectAdapter;
import org.kkonoplev.bali.runner.ProjectBuilder;

public class TestNGProjectAdapter implements ProjectAdapter {

	private TestNGProjectBuilder projectBuilder;
	
	public TestNGProjectAdapter(){
		projectBuilder = new TestNGProjectBuilder();
	}
	

	@Override
	public ProjectBuilder getProjectBuilder() {
		return projectBuilder;
	}


	@Override
	public BaseProject buildEmptyProject() {
		return new TestNGProject();
	}
	
	

}
