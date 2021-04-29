package org.kkonoplev.bali.runner.junit;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.kkonoplev.bali.suiteexec.annotation.Tag;
import org.kkonoplev.bali.suiteexec.annotation.UseResources;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;
import org.kkonoplev.bali.suiteexec.resource.ResourcePool;
import org.kkonoplev.bali.suiteexec.resource.ResourcePoolFactory;
import org.kkonoplev.bali.suiteexec.resource.TestExecResource;

public class JUnitProjectBuilder implements ProjectBuilder {

private static final Logger log = Logger.getLogger(ProjectBuilder.class);
	
protected ExtFileFilter jarFileFilter = new ExtFileFilter(".jar");
protected ExtFileFilter testFileFilter = new ExtFileFilter("test.class");

protected ProjectService projectSvc;
protected JUnitProject project;

public JUnitProjectBuilder(){
	
}

public JUnitProjectBuilder(ProjectService projectSvc_){
	
}

public BaseProject createProjectFromDir(String name, String binpath, String libpath, HashMap<String, String[]> factory, String parentProjectName) throws Exception {
	
	JUnitProject proj = new JUnitProject(name);
	
	proj.setBinfolder(binpath);
	proj.setLibfolder(libpath);		
	
	BaseProject parentProject = projectSvc.getProject(parentProjectName);
	proj.setParentProject(parentProject);
	
	init(proj);
	
	return proj;		
}

public void init(BaseProject proj) throws Exception  {
	
	this.project = (JUnitProject) proj;
	
	log.info("init project:"+project.getName());
	initClassLoader();
	initRunnableTree();
	initResourceStorages();
	initResultsExporter(project.getPluggableProcessorStore(), project.getClassLoader());
	project.setLoadDate(new Date());
	
}

private void initResourceStorages() throws Exception {
	log.info("Init resource storages");
	for (ResourcePool storage: project.getResourceStorages())
		initResourceStorage(storage, project.getClassLoader());
	
}

private void initClassLoader() throws Exception {

	log.info("Init ClassLoader");
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
	
	log.info("Init Runnable Tree");
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
	
	log.info("Found tests: "+project.getTestcount());
	
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
	
	log.info("ResourceFactory "+factoryName);
	
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

	log.info("Init Resources");
	for (String loadcmd: storage.getLoadcmds()){
		log.info("loadcmd = "+loadcmd);
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

	log.info("Scanning folder structure for runnable items "+binfolder);
	int testFound = 0;
	
	TreeNode folderNode = new TreeNode(binfolder.getName());		
	ArrayList<TreeNode> childs = new ArrayList<TreeNode>();
	
	File[] files = binfolder.listFiles(testFileFilter);
	for (File f: files){
		
		if (f.isDirectory()){
			TreeNode child = getFolderStructure(f, testFileFilter, projpath);
			if (child != null)
				childs.add(child);
			
		} else {
			
			String testpath = f.getPath();
			String name = ClassUtil.getClassName(projpath, testpath);
			//log.info("add test:"+name);
			
			Class testclazz = project.getClassLoader().loadClass(name);
			String simpleName = testclazz.getSimpleName();
			
			TreeNode testNode = new TreeNode(name, name);
			testNode.setLineViewLabel(name);
			testNode.setLabel(simpleName);
			testNode.setName(simpleName);
			testNode.setTitle("JUnit test");
			testNode.setTags(parseTagsList(testclazz));

			JUnitRunnableItem item = new JUnitRunnableItem();
		    item.setProject(project);
		    item.setTestClass(testclazz);
		    item.setRequiredTestResources(parseRequiredClassResourcesList(testclazz));
		    testNode.setRunnableItem(item);
		       
			childs.add(testNode);
			testFound++;
			
		}
	}

	if (childs.size() > 0){
		
		Collections.sort(childs, 
				new Comparator<TreeNode>() {
					@Override
					public int compare(TreeNode n1, TreeNode n2) {
						return n1.getName().compareTo(n2.getName());
					}
				});
		folderNode.setChilds(childs);
		
		if (testFound > 0)
			log.info("Tests found: "+testFound);
		
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

private List<String> parseTagsList(Class testclass){

	List<String> tags = new ArrayList<String>();
	
	if (testclass == null)
		return tags;
	
	Tag[] tagsAnn = (Tag[]) testclass.getAnnotationsByType(Tag.class);
	if (tagsAnn != null)
		for (int i = 0; i < tagsAnn.length; i++)
			tags.add(tagsAnn[i].name());			
	
	
	return tags;
}



private URLClassLoader createClassLoader(String binfolderpath, String libfolderpath, URLClassLoader parentLoader) throws Exception {
	
	log.info("bin folder: "+binfolderpath);
	log.info("lib folder: "+libfolderpath);
	
	
	File binfolder = new File(binfolderpath);
	
	ArrayList<URL> urls = new ArrayList<URL>();
	
	if (libfolderpath.contains(";")){
		fillUrls(libfolderpath, urls);
	} else {
		File libfolder = new File(libfolderpath);
		fillUrls(libfolder, urls, jarFileFilter);
	}
	
	urls.add(binfolder.toURI().toURL());
	
	URL[] urls_ = new URL[urls.size()];
	urls.toArray(urls_);
	
	URLClassLoader classLoader;
	
	log.info("List of classes to load:");
	int i = 1;
	for (URL url: urls_)
		log.info((i++)+"."+url.toURI().toString());
	
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

	log.info("Scanning listing of folders  "+libfolder.toString());

	File[] files = libfolder.listFiles(jarFileFilter);
	
	if (files == null)
		log.error("NULL files were found in lib folder: "+libfolder.getAbsolutePath());
	
	if (files.length == 0)
		log.error("0 files were found in lib folder: "+libfolder.getAbsolutePath());
	
	
	for (File f: files){
		if (f.isDirectory()){
			fillUrls(f, urls, jarFileFilter);
		} else {
			urls.add(f.toURI().toURL());
		}
	}
	
}

private void fillUrls(String libpath, ArrayList<URL> urls) throws Exception {
	
	log.info("Splitting libpath var by \";\" to get list of directly pointed jars");
	String[] libpaths = libpath.trim().split(":");
	
	for (String jarpath: libpaths){
		File jarFile = new File(jarpath);
		urls.add(jarFile.toURI().toURL());
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
