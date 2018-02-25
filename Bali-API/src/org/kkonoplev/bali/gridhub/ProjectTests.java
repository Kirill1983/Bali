package org.kkonoplev.bali.gridhub;

import java.util.ArrayList;

public class ProjectTests {
	
	String project;
	ArrayList<String> tests = new ArrayList<String>();
	
	public ProjectTests(){
		
	}
	
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public ArrayList<String> getTests() {
		return tests;
	}
	public void setTests(ArrayList<String> tests) {
		this.tests = tests;
	}

	
}
