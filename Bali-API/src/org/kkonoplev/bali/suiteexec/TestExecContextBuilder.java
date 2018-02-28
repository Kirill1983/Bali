package org.kkonoplev.bali.suiteexec;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.project.structure.TreeNode;
import org.kkonoplev.bali.runner.RunnableItem;
import org.kkonoplev.bali.services.ProjectService;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;

public class TestExecContextBuilder {
	
	private static final Logger log = LogManager.getLogger(TestExecContextBuilder.class);
	
	public TestExecContextBuilder(){
	}
	
	
	public ArrayList<TestExecContext> build(SuiteExecContext suiteExecContext, String testDesc, ArrayList<TestExecContext> addedContexts){
		
 		String[] testInfo = testDesc.split("#");
		String projectName = testInfo[0];
		String path = testInfo[1];
		int threads = Integer.valueOf(testInfo[2]);
		SuiteMdl suiteMdl = suiteExecContext.getSuiteMdl();
		
		ArrayList<TestExecContext> testExecContexts = new ArrayList<TestExecContext>(threads);
		
		for (int thread = 1; thread <= threads; thread++){
			TestExecContext testExecContext = build(suiteExecContext, projectName, path, thread);
		
			List<String> dependsClassesChain = buildDependsChain(testExecContext);
			int size = dependsClassesChain.size();
			for (int i = 0; i < size ; i++){
				String depClass = dependsClassesChain.get(size-i-1);
				if (!dependedClassAdded(depClass, addedContexts)){
					TestExecContext depTestExecContext = build(suiteExecContext, projectName, depClass, thread);
					testExecContexts.add(depTestExecContext);	
				}
			}
			
			testExecContexts.add(testExecContext);
		}
		
		return testExecContexts;
			
	}
	
	private List<String> buildDependsChain(TestExecContext testExecContext) {
		
		SuiteExecContext suiteExecContext = testExecContext.getSuiteExecContext();
		ProjectService projectSvc = suiteExecContext.getExecProcessor().getProjectSvc();
		BaseProject project = projectSvc.getProject(testExecContext.getProjectName());
		ArrayList<String> depChain = new ArrayList<String>();
		
		String depOnClass = testExecContext.getRunnableNode().getDependsOnNodeClass();
		while (!depOnClass.equals("")){
			depChain.add(depOnClass);
			TreeNode treeNode = project.getTreeNode(depOnClass);
			depOnClass = treeNode.getDependsOnNodeClass();
		}
		
		return depChain;
		
	}


	private boolean dependedClassAdded(String depOnClassName,
			ArrayList<TestExecContext> addedContexts) {

		for (TestExecContext testContext: addedContexts){
			testContext.getRunnableNode().getFullName().equals(depOnClassName);
			return true;
		}
		
		return false;
	}


	public TestExecContext build(SuiteExecContext suiteExecContext, String projectName, String className, int threadId){
		
		TestExecContext testExecContext = new TestExecContext();
		testExecContext.setThreadId(threadId);
		testExecContext.setProjectName(projectName);
		testExecContext.setClassName(className);
		testExecContext.setSuiteExecContext(suiteExecContext);
		testExecContext.getTreeLog().setName(className);

		SuiteMdl suiteMdl = suiteExecContext.getSuiteMdl();
		testExecContext.setCaptureMode(suiteMdl.isCapturemode());
		testExecContext.setDebugMode(suiteMdl.isDebugmode());
		reset(testExecContext);
		
		log.info("Builder TestExecContext: "+testExecContext);
		return testExecContext;
		
	}
	
	public void reset(TestExecContext testExecContext) {
		
		testExecContext.setStartDate(null);
		testExecContext.setEndDate(null);
		testExecContext.setStatus(TestExecContextState.WAITING);
		testExecContext.setErrorCount(0);
		
		SuiteExecContext suiteExecContext = testExecContext.getSuiteExecContext();
		ProjectService projectSvc = suiteExecContext.getExecProcessor().getProjectSvc();
		BaseProject project = projectSvc.getProject(testExecContext.getProjectName());
		
		if (project == null){
			testExecContext.setStatus(TestExecContextState.PROJECT_MISSING);
			return;
		}

		
		try {

			TreeNode treeNode = project.getTreeNode(testExecContext.getClassName());
			testExecContext.setRunnableNode(treeNode);
			testExecContext.setRunnableItem(treeNode.getRunnableItem());
			
		} catch (Exception e){
			log.warn(e,e);
			testExecContext.setStatus(TestExecContextState.ERROR_RUNNABLE_UNIT);
		}
	
		updateResourceStatus(testExecContext);
	}

	
	private void updateResourceStatus(TestExecContext testExecContext) {

		String allres = "";

		if (testExecContext.getRunnableItem() == null)
			return;
		
		RunnableItem runnableItem = testExecContext.getRunnableItem();
		
		for (RequiredTestResource reqResource: runnableItem.getRequiredTestResources()){
			allres += reqResource.getTestExecResourceClass().getSimpleName()+" "+reqResource.getProperties()+", ";
		}
		
		if (allres.length()>=2)
			allres = allres.substring(0, allres.length() - 2);

		testExecContext.setResourceStatus(allres);

	}
	
}
