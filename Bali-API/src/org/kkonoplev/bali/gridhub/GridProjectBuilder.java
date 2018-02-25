package org.kkonoplev.bali.gridhub;

import java.util.ArrayList;
import java.util.Date;

import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.project.structure.TreeNode;
import org.kkonoplev.bali.services.ProjectTests;

public class GridProjectBuilder {

public static TreeBuilder treeBuilder = new TreeBuilder();
	
	public BaseProject build(ProjectTests prTests){
		BaseProject basePr = new BaseProject();
		basePr.setName(prTests.getProject());	
		basePr.setLoadDate(new Date());
		TreeNode rootNode = treeBuilder.build(prTests.getTests());
		basePr.setRootFolderNode(rootNode);
		return basePr;
	}

	public ArrayList<BaseProject> build(ProjectTests[] prTestsList) {
		ArrayList<BaseProject> projects = new ArrayList<BaseProject>();
		for (ProjectTests pt: prTestsList)
			projects.add(build(pt));
		return projects;
	}
}
