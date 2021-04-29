package org.kkonoplev.bali.runner.main;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.common.utils.ClassUtil;
import org.kkonoplev.bali.common.utils.ExtFileFilter;
import org.kkonoplev.bali.project.BaseJavaProject;
import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.project.structure.TreeNode;
import org.kkonoplev.bali.runner.ProjectBuilder;
import org.kkonoplev.bali.services.ProjectService;
import org.kkonoplev.bali.services.SuitePluggableProcessor;
import org.kkonoplev.bali.services.SuitePluggableProcessorStorage;
import org.kkonoplev.bali.suiteexec.annotation.UseResources;
import org.kkonoplev.bali.suiteexec.resource.ResourcePoolFactory;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;
import org.kkonoplev.bali.suiteexec.resource.ResourcePool;
import org.kkonoplev.bali.suiteexec.resource.TestExecResource;

public class ExtHandlerProjectBuilder implements ProjectBuilder {

private static final Logger log = Logger.getLogger(ProjectBuilder.class);
	
protected ExtFileFilter jarFileFilter = new ExtFileFilter(".jar");
protected ExtFileFilter testFileFilter = new ExtFileFilter("test.class");

protected ProjectService projectSvc;
protected MainProject project;

public ExtHandlerProjectBuilder(){
	
}

public ExtHandlerProjectBuilder(ProjectService projectSvc_){
	
}

public BaseProject createProjectFromDir(String name, String binpath, String libpath, HashMap<String, String[]> factory, String parentProjectName) throws Exception {
	
	MainProject proj = new MainProject(name);
	
	proj.setBinfolder(binpath);
	proj.setLibfolder(libpath);		
	
	BaseProject parentProject = projectSvc.getProject(parentProjectName);
	proj.setParentProject(parentProject);
	
	init(proj);
	
	return proj;		
}


public void init(BaseProject proj) throws Exception  {
	
	this.project = (MainProject) proj;
	
	log.info("init project:"+project.getName());
	
	initClassLoader();
	initRunnableTree();
	initResourceStorages();
	initResultsExporter(project.getPluggableProcessorStore(), project.getClassLoader());
	project.setLoadDate(new Date());
	
}

private void initResourceStorages() throws Exception {
	for (ResourcePool storage: project.getResourceStorages()){
		initResourceStorage(storage, project.getClassLoader());
	}
}

private void initClassLoader() throws Exception {

	// init classLoader
	URLClassLoader parentLoader = null;
	if (project.getParentProject() != null){
		if (project.getParentProject() instanceof BaseJavaProject)
			parentLoader = ((BaseJavaProject)project.getParentProject()).getClassLoader();		
	}
	
	URLClassLoader classLoader = createClassLoader(project.getBinfolder(), project.getLibfolder(), parentLoader);		
	project.setClassLoader(classLoader);

	
}

private void initRunnableTree() throws Exception {
	
	TreeNode rootNode = getFolderStructure(new File(project.getBinfolder()), new ExtFileFilter(project.getTestmatch()), project.getBinfolder());
	TreeNode shortRootNode = getShortRootNode(rootNode);
	project.setRootFolderNode(shortRootNode);	
	
	String prepath = getPrePath(shortRootNode, rootNode);
	if (prepath.indexOf(".") > 0){
		prepath = prepath.substring(prepath.indexOf(".")+1);
	}
	project.setPrepath(prepath);
	
	updateLeafsPath(shortRootNode);
	project.setTestcount(leafcount(shortRootNode));
	
}

private void updateLeafsPath(TreeNode rootNode) {
	
	if (rootNode == null)
		return;
	
	for (TreeNode childNode: rootNode.getChilds())
		updateLeafsPathName("", childNode);

}

private void updateLeafsPathName(String prePath, TreeNode rootNode) {
	
	String nodeName = rootNode.getName();
	
	if (prePath.length() > 0)
		prePath = prePath+"."+nodeName;
	else
		prePath = nodeName;
	
	if (rootNode.getChilds().size() == 0){
		rootNode.setPath(prePath);
	} else {
		
		for (TreeNode childNode: rootNode.getChilds()){
			updateLeafsPathName(prePath, childNode);
		}
		
	}
}

private String getPrePath(TreeNode node, TreeNode rootNode) {

	if (node == null)
		return "";
	
	if (node == rootNode){
		return node.getName()+".";
	} else 
		return rootNode.getName()+"."+getPrePath(node, rootNode.getChilds().get(0));
	
	
}

private int leafcount(TreeNode node) {
	if (node == null)
		return 0;
	
	//if is leaf
	if (!node.isFolder())
		return 1;
	
	// if is folder
	int sum = 0;
	for (TreeNode child: node.getChilds())
			sum += leafcount(child);
		
	return sum;
}

private void initResultsExporter(SuitePluggableProcessorStorage plugProcessorStore,	URLClassLoader classLoader) throws Exception {

	try {
		
		String className = plugProcessorStore.getClassName();
		if (className.equals(""))
			return;
	
		Class<? extends SuitePluggableProcessor> plugprocessorclass = (Class<? extends SuitePluggableProcessor>) classLoader.loadClass(plugProcessorStore.getClassName());
		SuitePluggableProcessor plugProcessor = plugprocessorclass.newInstance();
		plugProcessorStore.setSuitePluggableProcessor(plugProcessor);
		
	} catch (Exception e){
		log.error(e, e );
	}
	
}

private void initResourceStorage(ResourcePool storage, URLClassLoader classLoader) throws Exception {
	
	// init factory
	//clear all resources were before
	storage.getResources().clear();
	String factoryName = storage.getFactoryClassName();
	
	Class<? extends ResourcePoolFactory> factoryclass = (Class<? extends ResourcePoolFactory>) classLoader.loadClass(factoryName);
	ResourcePoolFactory factory;
	factory = factoryclass.getConstructor().newInstance();
	
	storage.setFactory(factory);
	
	// init resources
	initStorageResources(storage);
}

/*
 * load resources to storage
 */
private void initStorageResources(ResourcePool storage) {
	
	for (String loadcmd: storage.getLoadcmds()){
		ArrayList<TestExecResource> resources = storage.getFactory().load(loadcmd);
		storage.getResources().addAll(resources);
	}
	
}


private TreeNode getShortRootNode(TreeNode rootNode) {
	
	if (rootNode == null)
		return null;
		
	if (rootNode.getChilds().size() == 1)
		return getShortRootNode(rootNode.getChilds().get(0));
	else
		return rootNode;

}

private TreeNode getFolderStructure(File binfolder,	ExtFileFilter testFileFilter, String projpath) throws ClassNotFoundException {

	TreeNode folderNode = new TreeNode(binfolder.getName());		
	ArrayList<TreeNode> childs = new ArrayList<TreeNode>();
	
	File[] files = binfolder.listFiles(testFileFilter);
	for (File f: files){
		
		if (f.isDirectory()){
			TreeNode child = getFolderStructure(f, testFileFilter, projpath);
			if (child != null)
				childs.add(child);
			
		} else {
			
			String testpath = f.getAbsolutePath();
			String name = ClassUtil.getClassName(projpath, testpath);
			
			Class testclazz = project.getClassLoader().loadClass(name);
			String simpleName = testclazz.getSimpleName();
			
			TreeNode testNode = new TreeNode(name, name);
			testNode.setLineViewLabel(name);
			testNode.setLabel(simpleName);
			testNode.setName(simpleName);
			testNode.setTitle("JUnit test");
			
			MainRunnableItem item = new MainRunnableItem();
		    item.setProject(project);
		    item.setTestClass(testclazz);
		    item.setRequiredTestResources(parseRequiredClassResourcesList(testclazz));
		    testNode.setRunnableItem(item);
		       
			childs.add(testNode);
			
		}
	}

	if (childs.size() > 0){
		folderNode.setChilds(childs);
		return folderNode;
	}
	
	return null;
}
		
	

private List<RequiredTestResource> parseRequiredClassResourcesList(Class testclass){

		List<RequiredTestResource> resources = new ArrayList<RequiredTestResource>();
		
		if (testclass == null)
			return resources;
		
		UseResources useResources = (UseResources) testclass.getAnnotation(UseResources.class);
		if (useResources != null)
			for (int i = 0; i < useResources.list().length; i++){
				RequiredTestResource reqTestResource = new RequiredTestResource(useResources.list()[i], "");
				resources.add(reqTestResource);			
			}
		
		List<RequiredTestResource> resourcesChild;
		
		resourcesChild = parseRequiredClassResourcesList(testclass.getSuperclass());
		resources.addAll(resourcesChild);

		return resources;
}




private URLClassLoader createClassLoader(String binfolderpath, String libfolderpath, URLClassLoader parentLoader) throws Exception {
	
	File binfolder = new File(binfolderpath);
	File libfolder = new File(libfolderpath);
	
	ArrayList<URL> urls = new ArrayList<URL>();
	
	fillUrls(libfolder, urls, jarFileFilter);
	
	urls.add(binfolder.toURI().toURL());
	
	URL[] urls_ = new URL[urls.size()];
	urls.toArray(urls_);
	
	URLClassLoader classLoader;
	
	if (parentLoader != null)
		classLoader = new URLClassLoader(urls_, parentLoader);
	else{
		// running on tomcat is not placing context WebApp classloader
		// forcing pointer as parent directly
		classLoader = new URLClassLoader(urls_, this.getClass().getClassLoader());
	}
		
	
	return classLoader;
}

private void fillUrls(File libfolder, ArrayList<URL> urls, FileFilter jarFileFilter) throws Exception {
	
	File[] files = libfolder.listFiles(jarFileFilter);
	
	for (File f: files){
		if (f.isDirectory()){
			fillUrls(f, urls, jarFileFilter);
			
		} else {
			urls.add(f.toURI().toURL());
		}
	}
	
}

@Override
public void setProjectService(ProjectService projectService) {
	this.projectSvc = projectService;		
}

@Override
public void updateInfo(String content) {
	
	
}
	
	
}
