/* 
 * Copyright � 2011 Kirill Konoplev
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

package org.kkonoplev.bali.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.common.utils.ReflectionUtil;
import org.kkonoplev.bali.project.BaseJavaProject;
import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.gridhub.*;
import org.kkonoplev.bali.project.BuildConfig;
import org.kkonoplev.bali.project.structure.TreeNode;
import org.kkonoplev.bali.runner.ProjectAdapter;
import org.kkonoplev.bali.runner.ProjectBuilder;
import org.kkonoplev.bali.suiteexec.TestExecContext;
import org.kkonoplev.bali.suiteexec.resource.EnvResource;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;
import org.kkonoplev.bali.suiteexec.resource.ResourcePool;
import org.kkonoplev.bali.suiteexec.resource.TestExecResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.google.gson.Gson;



public class ProjectService {
	
	private static final Logger log = Logger.getLogger(ProjectService.class);
	
	protected ArrayList<BaseProject> projects = new ArrayList<BaseProject>();

	public ProjectService(String file) throws Exception {
		try {
			log.info("Parse projects config "+file);
			Document doc = parseDocument(file);	   
			Node node = doc.getFirstChild();
		    int size = node.getChildNodes().getLength();
		    
			for (int i = 0; i < size; i++)	{
				Node child = node.getChildNodes().item(i);
				if (child.getNodeName().equalsIgnoreCase("project")){
					BaseProject proj = parseProject(child);
					init(proj);
					projects.add(proj);
				}
			}
			log.info("Parse projects config done succ");
		} catch (Exception e){
			log.error(e, e);
		}
		
	    
	}
	
	public ArrayList<ProjectTests> getProjectTestsList(){
		
		ArrayList<ProjectTests> prTests = new ArrayList<ProjectTests>();
		for (BaseProject project : projects){
			ProjectTests pt = new ProjectTests();
			pt.setProject(project.getName());
			pt.setTests(buildFlatTestsList(project.getRootFolderNode()));
			prTests.add(pt);
		}
		
		return prTests;
	}
	
	private ArrayList<String> buildFlatTestsList(TreeNode rootNode) {
		ArrayList<String> tests = new ArrayList<String>();
		traverseAndAddChildPath(rootNode, tests);	
		return tests;
	}

	private void traverseAndAddChildPath(TreeNode node, ArrayList<String> tests) {
		if (node == null)
			return; 
		
		if (node.isLeaf()){
			tests.add(node.getPath());
			log.info("adding "+node.getPath());
			return;
		}
		
		for (TreeNode child : node.getChilds())
			traverseAndAddChildPath(child, tests);
	}

	private void init(BaseProject proj) throws Exception {
		log.info("Init project = "+proj.getName());
		ProjectBuilder projectBuilder = proj.getProjectAdapter().getProjectBuilder();
		projectBuilder.setProjectService(this);
		projectBuilder.init(proj);
		
		log.info("runnable items found = "+proj.getTestcount());
		log.info("resource pools found = "+proj.getResourceStorages().size());

	}

	

	private BaseProject initEmptyProjectFromAdapterClass(String projectRulesClass) throws Exception {
	
		log.info("Init project from rules class");
		Class clazz = Class.forName(projectRulesClass);
		ProjectAdapter projectAdapter = (ProjectAdapter) clazz.newInstance();
		BaseProject project = projectAdapter.buildEmptyProject();
		project.setProjectAdapter(projectAdapter);
		return project;
	}
	
	private Document parseDocument(String file) throws Exception {
		//parse configuration projects.xml
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource( new FileReader(file));
	    Document d = builder.parse(is);	
	    return d;
	}

	/*
	 * parse <project> </project> and return Project object
	 */
	private BaseProject parseProject(Node entry) throws Exception {
		
		log.info("");
		log.info("parse project");
		
		BaseProject proj = new BaseProject(); 
		ArrayList<ResourcePool> storages = new ArrayList<ResourcePool>();
		
		int size = entry.getChildNodes().getLength();
		    
		for (int i = 0; i < size; i++)	{
			Node child = entry.getChildNodes().item(i);
			
			if (child.getNodeName().equalsIgnoreCase("adapter")){
				log.info("adapter="+child.getTextContent());
				proj = initEmptyProjectFromAdapterClass(child.getTextContent());
				
			}
				
			if (child.getNodeName().equalsIgnoreCase("Config"))
				parseProjectConfig(child, proj);
			
			if (child.getNodeName().equalsIgnoreCase("ResourcePool")){
				ResourcePool storage = parseResourcePool(child);
				storages.add(storage);
			}
			
			if (child.getNodeName().equalsIgnoreCase("PluggableProcessorStorage")){
				
				SuitePluggableProcessorStorage pluggableProcessorStorage = parseExporter(child);
				proj.setPluggableProcessorStore(pluggableProcessorStorage);

			}
			
		}	
		
		proj.setResourceStorages(storages);
		log.info("parse project done");
		
		return proj;
		
	}

	private BuildConfig parseBuildConfig(Node entry) {
		
		log.info("Parse build config");
		BuildConfig buildConfig = new BuildConfig();

		int size = entry.getChildNodes().getLength();	
	    
		for (int i = 0; i < size; i++)	{
			
			Node child = entry.getChildNodes().item(i);
			
			if (child.getNodeName().equalsIgnoreCase("command")){
				buildConfig.setCommand(child.getTextContent());
				log.info("Command="+child.getTextContent());
			}
				
			if (child.getNodeName().equalsIgnoreCase("SuccessMark")){
				buildConfig.setSuccessMark(child.getTextContent());
				log.info("SuccessMark="+child.getTextContent());
			}
			
		}			
		
		return buildConfig;
		
	}

	private SuitePluggableProcessorStorage parseExporter(Node entry) {
		
		SuitePluggableProcessorStorage exporter = new SuitePluggableProcessorStorage();
		
		int size = entry.getChildNodes().getLength();	
	    
		for (int i = 0; i < size; i++)	{
			
			Node child = entry.getChildNodes().item(i);
			if (child.getNodeName().equalsIgnoreCase("class"))
				exporter.setClassName(child.getTextContent());
			
			if (child.getNodeName().equalsIgnoreCase("defaultmeta"))
				exporter.setDefaultmeta(child.getTextContent());
			
		}			
		
		return exporter;
		
	}

	private ResourcePool parseResourcePool(Node entry) {
		log.info("parse resource pool");
		ResourcePool storage = new ResourcePool();
		
		int size = entry.getChildNodes().getLength();	
	    
		for (int i = 0; i < size; i++)	{
			
			Node child = entry.getChildNodes().item(i);
			if (child.getNodeName().equalsIgnoreCase("factoryclass")){
				storage.setFactoryClassName(child.getTextContent());
				log.info("factory class="+child.getTextContent());
			}
			
			if (child.getNodeName().equalsIgnoreCase("loadcmd")){
				storage.getLoadcmds().add(child.getTextContent());
				log.info("load cmd="+child.getTextContent());
			}
		}	
		
		
		return storage;
		
	}

	private void parseProjectConfig(Node entry, BaseProject proj) throws Exception {
		log.info("parse Config");
		for (int i = 0; i < entry.getChildNodes().getLength(); i++){
			
		
			Node child = entry.getChildNodes().item(i);
		
			if (child.getNodeName().equals("adapter")){
				log.info("adapter="+child.getTextContent());
				proj = initEmptyProjectFromAdapterClass(child.getTextContent());
			} else
			if (child.getNodeName().equals("name")){
				log.info("name="+child.getTextContent());
				proj.setName(child.getTextContent());
			}
			if (child.getNodeName().equals("detailconfig")){
				log.info("detailconfig="+child.getTextContent());
				setFields(proj, child);
			} 
			else if (child.getNodeName().equals("parentproject")){
				log.info("parenproject="+child.getTextContent());
				proj.setParentProject(getProject(child.getTextContent()));
			}
			else if(child.getNodeName().equalsIgnoreCase("buildconfig")){
				BuildConfig buildConfig = parseBuildConfig(child);
				proj.setBuildConfig(buildConfig);
			}

			

			
		}
		
	}

	private void setFields(BaseProject proj, Node entry) {
		//ReflectionUtil
		
		log.info("parse Config");
		for (int i = 0; i < entry.getChildNodes().getLength(); i++){
			Node node = entry.getChildNodes().item(i);
			String methodName = node.getNodeName();
			String value = node.getTextContent();
			if (methodName.contains("#"))
				continue;
				
			try {
				ReflectionUtil.setValue(proj, methodName, value);
			} catch (Exception e){
				log.error("Error calling set"+methodName+" with "+value+" for project "+proj.getName()+" type "+proj.getClass().getSimpleName());
			}
		}
		
	}

	public ArrayList<BaseProject> getProjects() {
		return projects;
	}
	
	public ArrayList<BaseProject> getGridProjects() throws Exception {
		
		 URL url = new URL("http://localhost:8080/bali/form/status/projects/json");
	     URLConnection urlcon = url.openConnection();
	     BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                urlcon.getInputStream()));
	     StringBuffer buf = new StringBuffer();
	     String line;

	     while ((line = in.readLine()) != null) 
	            buf.append(line);
	     in.close();
	    
	     
	     Gson gson = new Gson();
	     //System.out.println(buf.toString());
		 ProjectTests[] prTestsOut = gson.fromJson(buf.toString(), ProjectTests[].class);
		
		 GridProjectBuilder  gpBuilder = new GridProjectBuilder();
		 ArrayList<BaseProject> projects = gpBuilder.build(prTestsOut);
		 return projects;
	}


	public void setProjects(ArrayList<BaseProject> projects) {
		this.projects = projects;
	}
	
	public BaseProject getProject(String projectName){
		
		if (projectName == null)
			return null;
		
		for (BaseProject proj: projects)
			if (proj.getName().equalsIgnoreCase(projectName))
				return proj;
		
		return null;
		
	}

	//reload project and its child projects automatically
	public void reload(BaseProject proj) throws Exception {
		
		proj.getProjectAdapter().getProjectBuilder().init(proj);
		
		for (BaseProject project: projects)			
			// if it is a child project - reload it also!
			if (project.getParentProject() == proj)
				reload(project.getName());
		
	}
	
	public void reload(String name) throws Exception {
		
		BaseProject proj = getProject(name);
		
		if (proj == null){
			log.warn("No project found for name: "+name);		
			return;
		}
	
		reload(proj);		
		
	}
	public void dinit(){
		
	}

	
	/*
	 * seach project by classloader
	 */
	public BaseProject getProject(ClassLoader loader){
		
		for (BaseProject proj: projects){
			if (proj instanceof BaseJavaProject){
				if (((BaseJavaProject)proj).getClassLoader() == loader)
					return proj;
			}
		}
		
		return null;
	}

	public void updateInfo(String projectName, String content){
		BaseProject project = getProject(projectName);
		project.getProjectAdapter().getProjectBuilder().updateInfo(content);
		
	}
	
	
	public TestExecResource getFreeResource(RequiredTestResource requiredTestResource, TestExecContext testExecContext){
		
		// search factory for this test exec resource class
		ResourcePool storage = findStorage(requiredTestResource.getTestExecResourceClass(), testExecContext.getProjectName());
		
		if (storage == null)
			return null;
		
		return storage.getFreeResource(testExecContext, requiredTestResource.getProperties());
			
	}
	

	public TestExecResource getTestExecResource(TestExecContext testExecContext, RequiredTestResource requiredTestResource ) {
		
		// search factory for this test exec resource class
		ResourcePool storage = findStorage(requiredTestResource.getTestExecResourceClass(), testExecContext.getProjectName());
		
		if (storage == null)
			return null;
		
		return storage.getResource(testExecContext);
		
	}
	
	public TestExecResource getTestExecResource(TestExecContext testExecContext, Class<? extends TestExecResource> resourceClass) {
		
		// search factory for this test exec resource class
		ResourcePool storage= findStorage(resourceClass, testExecContext.getProjectName());
		
		if (storage == null)
			return null;
		
		return storage.getResource(testExecContext);
		
	}
	
	private ResourcePool findStorage(Class<? extends TestExecResource> resourceclass, String topProject) {
		// resource from project 
		BaseProject proj = getProject(resourceclass.getClassLoader());		
		if (proj == null){
			if (resourceclass.equals(EnvResource.class)){
				proj = getProject(topProject);
				ResourcePool storage = findStorageInProjectTree(proj, resourceclass);
				return storage;
			} else {
				return null;
			}
			
		}
		
		
		// search factory for this test exec resource class
		ResourcePool storage = findStorage(proj, resourceclass);
		return storage;
	}

	private ResourcePool findStorageInProjectTree(BaseProject proj,
			Class<? extends TestExecResource> resourceclass) {
	
		ResourcePool storage = findStorage(proj, resourceclass);
		
		if (storage == null){
			if (proj.getParentProject() != null)
				return findStorageInProjectTree(proj.getParentProject(), resourceclass);
			else
				return null;
		} else
			return storage;
		
		
	}

	/*
	 * search factory for corresponding resource
	 */
	public ResourcePool findStorage(BaseProject proj,
			Class<? extends TestExecResource> resourceclass) {
		
		for (ResourcePool storage: proj.getResourceStorages()){
			if (storage.getFactory().getResourceClass() == resourceclass)
				return storage;
		}
		
		return null;
	}
	
	/*
	 * search factory for corresponding resource
	 */
	public ResourcePool findStorage(BaseProject proj,
			String resourceclassname) {
		
		for (ResourcePool storage: proj.getResourceStorages()){
			if (storage.getFactory().getResourceClass().getName().equalsIgnoreCase(resourceclassname))
				return storage;
		}
		
		return null;
	}



	
	
	
	
	
	

}
