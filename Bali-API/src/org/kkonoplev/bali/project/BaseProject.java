/* 
 * Copyright © 2011 Kirill Konoplev
 * 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.kkonoplev.bali.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kkonoplev.bali.common.utils.DateUtil;
import org.kkonoplev.bali.project.structure.TreeNode;
import org.kkonoplev.bali.runner.ProjectAdapter;
import org.kkonoplev.bali.services.SuitePluggableProcessorStorage;
import org.kkonoplev.bali.suiteexec.resource.ResourcePool;


public class BaseProject implements Project {
	
	protected ProjectAdapter projectAdapter;
	protected String projectRulesClass;

	protected ArrayList<ResourcePool> resourceStorages;
	protected SuitePluggableProcessorStorage pluggableProcessorStore = new SuitePluggableProcessorStorage();
	protected BaseProject parentProject;

	//by default match tests in bin folder with *test.class
	protected String testmatch = "test.class";
	
	protected String name;
	protected int testcount = 0;

	protected BuildConfig buildConfig;
	protected TreeNode rootFolderNode;	
	protected Date loadDate;

	
	public BaseProject(){		
	}
	
	public BaseProject(String name_){
		name = name_;	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TreeNode getRootFolderNode() {
		return rootFolderNode;
	}

	public void setRootFolderNode(TreeNode rootFolderNode) {
		this.rootFolderNode = rootFolderNode;
	}

	public Date getLoadDate() {
		return loadDate;
	}

	public void setLoadDate(Date loadDate) {
		this.loadDate = loadDate;
	}


	public BaseProject getParentProject() {
		return parentProject;
	}

	public void setParentProject(BaseProject parentProject) {
		this.parentProject = parentProject;
	}

	
	public String getLoadDateFmt() {
		return DateUtil.dateFormat("MM/dd HH:mm:ss", loadDate);
	}

	public ArrayList<ResourcePool> getResourceStorages() {
		return resourceStorages;
	}

	public void setResourceStorages(ArrayList<ResourcePool> resourceStorages) {
		this.resourceStorages = resourceStorages;
	}

	public String getTestmatch() {
		return testmatch;
	}

	public void setTestmatch(String testmatch) {
		this.testmatch = testmatch;
	}

	public int getTestcount() {
		return testcount;
	}

	public void setTestcount(int testcount) {
		this.testcount = testcount;
	}

	public SuitePluggableProcessorStorage getPluggableProcessorStore() {
		return pluggableProcessorStore;
	}

	public void setPluggableProcessorStore(
			SuitePluggableProcessorStorage pluggableProcessorStore) {
		this.pluggableProcessorStore = pluggableProcessorStore;
	}

	public ProjectAdapter getProjectAdapter() {
		return projectAdapter;
	}

	public void setProjectAdapter(ProjectAdapter projectAdapter) {
		this.projectAdapter = projectAdapter;
	}

	public String getProjectRulesClass() {
		return projectRulesClass;
	}

	public void setProjectRulesClass(String projectRulesClass) {
		this.projectRulesClass = projectRulesClass;
	}

	public TreeNode getTreeNode(String path) {

		TreeNode currentNode = rootFolderNode;
		
		String steps[] = path.split("\\.");
		for (int i = 0; i < steps.length; i++){
			String leafName = steps[i];
			TreeNode childNode = currentNode.getNodeByName(leafName);
			
			if (childNode.getName().equals("")){
				throw new RuntimeException("Not able to find child with name "+leafName+" for node "+currentNode.getName());
			} else {
				currentNode = childNode;
			}

		}
		
		return currentNode;
		
	}
	
	

	public BuildConfig getBuildConfig() {
		return buildConfig;
	}

	public void setBuildConfig(BuildConfig buildConfig) {
		this.buildConfig = buildConfig;
	}

	public List<String> getTags() {
		
		List<String> tags = new ArrayList<String>();
		rootFolderNode.addAllTags(tags);		
		return tags;
	}

}
