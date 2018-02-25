package org.kkonoplev.bali.runner;

import org.kkonoplev.bali.project.BaseProject;

public interface ProjectAdapter {
	
	public ProjectBuilder getProjectBuilder();
	public BaseProject buildEmptyProject();	


}
