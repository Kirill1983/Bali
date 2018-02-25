package org.kkonoplev.bali.project;

import java.util.ArrayList;
import java.util.Date;

import org.kkonoplev.bali.project.structure.TreeNode;
import org.kkonoplev.bali.suiteexec.resource.ResourcePool;

public interface Project {
	
	public TreeNode getTreeNode(String path);
	
	public String getName();
	public void setName(String name);
	
	public Date getLoadDate();
	public void setLoadDate(Date loadDate);
	
	public TreeNode getRootFolderNode();
	public void setRootFolderNode(TreeNode rootFolderNode);
	
	public int getTestcount();
	public void setTestcount(int testcount);
	
	public ArrayList<ResourcePool> getResourceStorages();
	public void setResourceStorages(ArrayList<ResourcePool> resourceStorages);
	
	public String getTestmatch();
	public void setTestmatch(String testmatch);
	
}
