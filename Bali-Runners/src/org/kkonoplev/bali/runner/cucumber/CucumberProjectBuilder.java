package org.kkonoplev.bali.runner.cucumber;

import gherkin.parser.Parser;
import gherkin.util.FixJava;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
import org.kkonoplev.bali.suiteexec.resource.ResourcePoolFactory;
import org.kkonoplev.bali.suiteexec.resource.ResourceMarkerFactory;
import org.kkonoplev.bali.suiteexec.resource.ResourcePool;
import org.kkonoplev.bali.suiteexec.resource.TestExecResource;

public class CucumberProjectBuilder implements ProjectBuilder {

private static final Logger log = Logger.getLogger(ProjectBuilder.class);
	
	protected ProjectService projectSvc;
	protected CucumberProject project;
	public CucumberProjectBuilder(){
		
	}
	
	public CucumberProjectBuilder(ProjectService projectSvc_){
		
	}
	
	public BaseProject createProjectFromDir(String name, String binpath, String libpath, HashMap<String, String[]> factory, String parentProjectName) throws Exception {
		
		CucumberProject proj = new CucumberProject(name);
		
		proj.setBinfolder(binpath);
		proj.setLibfolder(libpath);		
		
		BaseProject parentProject = projectSvc.getProject(parentProjectName);
		proj.setParentProject(parentProject);
		
		
		init(proj);
		
		return proj;		
	}

	public void init(BaseProject project) throws Exception  {
		
		CucumberProject proj = (CucumberProject) project;
		log.info("init project:"+proj.getName());
		
		// init classLoader
		URLClassLoader parentLoader = null;
		if (proj.getParentProject() instanceof BaseJavaProject)
			parentLoader = ((BaseJavaProject)proj.getParentProject()).getClassLoader();		

		URLClassLoader classLoader = createClassLoader(proj.getBinfolder(), proj.getLibfolder(), parentLoader);		
		proj.setClassLoader(classLoader);	
		project = proj;
		
		// parse and init tests tree structure
		TreeNode rootNode = getFolderStructure(new File(proj.getBinfolder()), new ExtFileFilter(proj.getTestmatch()), proj.getBinfolder());
		TreeNode shortRootNode = getShortRootNode(rootNode);	
		String prepath = getPrePath(shortRootNode, rootNode);
		if (prepath.indexOf(".") > 0){
			prepath = prepath.substring(prepath.indexOf(".")+1);
		}
		
		proj.setPrepath(prepath);
		
		updateLeafsPath(shortRootNode);
		//waveTagsDown(new ArrayList<String>(), shortRootNode);
		
		proj.setRootFolderNode(shortRootNode);	
		
		// count test quantity
		//int leafcount = leafcount(shortRootNode);
		//proj.setTestcount(leafcount);
		
		
		
		//init ResourceStorages
		for (ResourcePool storage: proj.getResourceStorages()){
			initResourceStorage(storage, classLoader);
		}
	
		addPredefinedTagsResourceStorage(proj);
		
		initResultsExporter(proj.getPluggableProcessorStore(), classLoader);
		
		// set load date
		proj.setLoadDate(new Date());

		
	}
	
	/*
	 * set Tags from Parents to Children recursively
	 */

	private void addPredefinedTagsResourceStorage(BaseProject project) {
		
		if (project.getLoadDate() != null)
			project.getResourceStorages().remove(project.getResourceStorages().size()-1);
		
					
		List<String> tags = project.getTags();
		ResourcePool storage = new ResourcePool();
		ResourcePoolFactory factory = new ResourceMarkerFactory();
		ArrayList<TestExecResource> envResources = factory.load(tags.toString().substring(1,  tags.toString().length()-1));
		
		storage.setResources(envResources);
		storage.setFactory(factory);
		project.getResourceStorages().add(storage);
		
	}
	
	
	/*
	 * set Tags from Parents to Children recursively
	 */
	private void waveTagsDown(List<String> tagslist, TreeNode rootNode) {
		
		List<String> addList = new ArrayList<String>();
		addTagsFromOneListToOther(tagslist, rootNode.getTags());
		
		for (String tag: rootNode.getTags()){
			if (!tagslist.contains(tag)){
				tagslist.add(tag);
				addList.add(tag);
			}
		}
		
		for (TreeNode child: rootNode.getChilds()){
			waveTagsDown(tagslist, child);			
		}
	
		removeTagsMainList(tagslist, addList);
		
		if (rootNode.isLeaf()){
			if (rootNode.getTags().size() > 0){
				String tagsTxt = "Tags: "+rootNode.getTags().toString() + "\n";
				rootNode.setTitle(tagsTxt+rootNode.getTitle());
			}
		}
		
	}
	
	
	private void removeTagsMainList(List<String> tagslist, List<String> addList) {
		for (String tag: addList){
			if (tagslist.contains(tag))
				tagslist.remove(tag);
		}
		
	}

	private void addTagsFromOneListToOther(List<String> tagslist,
			List<String> tags) {

		for (String tag: tagslist){
			if (!tags.contains(tag))
				tags.add(tag);
		}
		
	}

	private void updateLeafsPath(TreeNode rootNode) {
		
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
		String factoryClassName = storage.getFactoryClassName();
		
		if (factoryClassName == null)
			return;
		
		Class<? extends ResourcePoolFactory> factoryclass = (Class<? extends ResourcePoolFactory>) classLoader.loadClass(factoryClassName);
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

	private TreeNode getFolderStructure(File binfolder,	ExtFileFilter testFileFilter, String projpath) throws Exception {

		try {
			
			TreeNode treeNode = getFolderStructureDo(binfolder,	testFileFilter, projpath);
			return treeNode;
			
		} catch (Exception e){
			e.printStackTrace();
			log.error(e, e);
		}
		
		TreeNode folderNode = new TreeNode(binfolder.getName().toUpperCase());
		folderNode.setLabel("parse dir failed");
		return folderNode;
		
	}
	
	private TreeNode getFolderStructureDo(File binfolder,	ExtFileFilter testFileFilter, String projpath) throws Exception {

		TreeNode folderNode = new TreeNode(binfolder.getName().toUpperCase());		
		ArrayList<TreeNode> childs = new ArrayList<TreeNode>();
		
		
		File[] files = binfolder.listFiles(testFileFilter);
		for (File f: files){
			
			if (f.isDirectory()){
				TreeNode child = getFolderStructure(f, testFileFilter, projpath);
				if (child != null)
					childs.add(child);
				
			} else {
				TreeNode featureTree = parseFeatureTree(f);
				childs.add(featureTree);
			}
			
		}
		
		if (childs.size() > 0){
			folderNode.setChilds(childs);
			return folderNode;
		}
		
		return null;
	}


	private TreeNode parseFeatureTree(File f) throws Exception {
		
		try {
			
			TreeNode leafNode = parseFeatureTreeDo(f);
			return leafNode;
			
		}	catch (Exception e){
			e.printStackTrace();
			log.error(e, e);
		}
		
		TreeNode featureNode = new TreeNode();
		featureNode.setLabel("parsefail_"+f.getName());
        featureNode.setColor("2915c4");
		return featureNode; 
		
		
		
	}
	
	private TreeNode parseFeatureTreeDo(File f) throws Exception {

        String path = f.getAbsolutePath();
        String gherkin = FixJava.readReader(new InputStreamReader(
                new FileInputStream(path), "UTF-8"));
        
        GerkTreeFormatter formatter = new GerkTreeFormatter(project);
        Parser parser = new Parser(formatter);
        parser.parse(gherkin, path, 0);
        formatter.done();
        formatter.close();

        TreeNode featureNode = formatter.getFeatureNode();
        
        
		return featureNode;

	}

	private URLClassLoader createClassLoader(String binfolderpath, String libfolderpath, URLClassLoader parentLoader) throws Exception {
		
		File binfolder = new File(binfolderpath);
		
		ArrayList<URL> urls = new ArrayList<URL>();
		
		
		fillUrls(libfolderpath, urls);
		
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

	private void fillUrls(String libpath, ArrayList<URL> urls) throws Exception {
		
		String[] libpaths = libpath.trim().split(";");
		
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
		
		log.info("Update project "+project.getName()+" content ");
		log.info(content);
		project.setLibfolder(content);
		project.setLoadDate(new Date());
		log.info("Libs path updated for project "+project.getName());
		log.info("ready for rebuild");
		
	}
	
	
}
