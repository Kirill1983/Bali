package org.kkonoplev.bali.suiteexec;

import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.project.structure.TreeNode;
import org.kkonoplev.bali.runner.RunnableItem;
import org.kkonoplev.bali.services.ProjectService;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;

public class TestExecContextLocalBuilder {
	
	private static final Logger log = LogManager.getLogger(TestExecContextLocalBuilder.class);
	
	public TestExecContextLocalBuilder(){
	}
	
	
	public ArrayList<TestExecContext> build(SuiteExecContext suiteExecContext, String testDesc){
		
 		String[] testInfo = testDesc.split("#");
		String projectName = testInfo[0];
		String path = testInfo[1];
		int threads = Integer.valueOf(testInfo[2]);
		SuiteMdl suiteMdl = suiteExecContext.getSuiteMdl();
		
		ArrayList<TestExecContext> testExecContexts = new ArrayList<TestExecContext>(threads);
		
		for (int thread = 1; thread <= threads; thread++){
			TestExecContext testExecContext = build(suiteExecContext, projectName, path, thread);			
			testExecContexts.add(testExecContext);
		}
		
		return testExecContexts;
			
	}
	
	public TestExecContext build(SuiteExecContext suiteExecContext, String projectName, String className, int threadId){
		
		TestExecContext testExecContext = new TestExecContext();
		testExecContext.setThreadId(threadId);
		testExecContext.setProjectName(projectName);
		testExecContext.setClassName(className);
		testExecContext.setSuiteExecContext(suiteExecContext);

		SuiteMdl suiteMdl = suiteExecContext.getSuiteMdl();
		testExecContext.setCaptureMode(suiteMdl.isCapturemode());
		testExecContext.setDebugMode(suiteMdl.isDebugmode());
		reset(testExecContext, className);
		
		log.info("Builder TestExecContext: "+testExecContext);
		return testExecContext;
		
	}
	
	public void reset(TestExecContext testExecContext, String className) {
		
		
		SuiteExecContext suiteExecContext = testExecContext.getSuiteExecContext();
		
		try {

			TreeNode treeNode = new TreeNode();
			treeNode.setPath(className);
			
			testExecContext.setRunnableNode(treeNode);
			
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
