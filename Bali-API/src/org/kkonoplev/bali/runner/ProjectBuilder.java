package org.kkonoplev.bali.runner;

import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.services.ProjectService;

public interface ProjectBuilder {
	
	public void init(BaseProject project) throws Exception;
	public void setProjectService(ProjectService projectService);
	public void updateInfo(String content);

}
