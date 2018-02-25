/* 
 * Copyright � 2011 Konoplev Kirill
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
 */

package org.kkonoplev.bali.suiteexec;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.kkonoplev.bali.classifyreport.WarningBuilder;
import org.kkonoplev.bali.classifyreport.WarningCaseArtifactsBuilder;
import org.kkonoplev.bali.classifyreport.htmlbuilder.ClassifyReportHTMLBuilder;
import org.kkonoplev.bali.classifyreport.htmlbuilder.ClassifyReportViewConfig;
import org.kkonoplev.bali.classifyreport.model.Warning;
import org.kkonoplev.bali.common.logger.ThreadsAppender;
import org.kkonoplev.bali.common.perfomance.PerfomanceReport;
import org.kkonoplev.bali.common.utils.DateUtil;
import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.project.structure.TreeNode;
import org.kkonoplev.bali.runner.RunnableItem;
import org.kkonoplev.bali.services.ProjectService;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;
import org.kkonoplev.bali.suiteexec.resource.TestExecResource;



public class TestExecContext implements Serializable {

	private static final Logger log = Logger.getLogger(TestExecContext.class);

	private SuiteExecContext suiteExecContext;
	private transient TestExecutor testExecutor = null;
	private transient ArrayList<TestExecResource> resources = new ArrayList<TestExecResource>(); 
	
	private String projectName = "";
	private String className = "";
	
	private TreeNode runnableNode;
	private transient RunnableItem runnableItem;
	
	private Date startDate = null;
	private Date endDate = null;
	
	private TestExecContextState status = TestExecContextState.WAITING;
	private int loops = 0;
	private Message exportResultMessage = new Message();
	
	private volatile boolean needStop = false;
	private volatile boolean debugMode = false;
	private volatile boolean captureMode = true;
	protected String resourceStatus = "";
	protected int threadId = 1;
	protected int errorCount = 0;
	protected int captureCount = 0;
	
	public static final String testClassPre = "org.kkonoplev.cloud.autotests.";

	public static final String fmt = "MM/dd HH:mm:ss";

	public TestExecContext() {

	}

	public TestExecContext(SuiteExecContext suiteExecContext, String projectName, String className, int subid)
			throws ClassNotFoundException {
		
		super();
		this.threadId = subid;
		this.projectName = projectName;
		this.className = className;
		this.suiteExecContext = suiteExecContext;
		reset();
		
		
	}
	

	
	public void setCurrentThreadLogAppendersFile() {

		log.info("Init logger start in:" + Thread.currentThread().getName());
		File reportDir = suiteExecContext.getResultDirFile();
		String addPath = "";
		if (className.lastIndexOf(".") != -1)
			addPath = className.substring(0, className.lastIndexOf(".")).replaceAll("\\.", "\\" + File.separator);

		File packageDir = new File(reportDir, addPath);

		// if run from Eclipse need to create dir self
		if (!packageDir.exists())
			packageDir.mkdirs();

		// set which logger to write
		
		String name = className.substring(className.lastIndexOf(".")+1);
		log.info("Writing thread "+Thread.currentThread().getName()+" logs to "+packageDir.getAbsolutePath()+"\\"+"TESTLOG-" + name + "-"+threadId+".html");
		MDC.put(ThreadsAppender.FILE, new File(packageDir, "TESTLOG-" + name + "-"+threadId+".html").getAbsolutePath());
			
	}
		
	public void reset() throws ClassNotFoundException {

		startDate = null;
		endDate = null;
		status = TestExecContextState.WAITING;
		errorCount = 0;
		captureCount = 0;
		resources = new ArrayList<TestExecResource>();
		
		ProjectService projectSvc = suiteExecContext.getExecProcessor().getProjectSvc();
		BaseProject project = projectSvc.getProject(projectName);
		
		if (project == null){
			status = TestExecContextState.PROJECT_MISSING;
			return;
		}

		
		try {

			TreeNode treeNode = project.getTreeNode(className);
			runnableNode = treeNode;
			runnableItem = runnableNode.getRunnableItem();
			
		} catch (Exception e){
			log.warn(e,e);
			status = TestExecContextState.ERROR_RUNNABLE_UNIT;
		}
	
	
	}

	
	private void updateResourceStatus() {

		if (isFinished())
			return;
		
		resourceStatus = "";

		if (runnableItem == null)
			return;
		
		if (runnableItem.getRequiredTestResources().size() == 0)
			return;
		
		if (resources != null && resources.size() > 0){		
			for (TestExecResource resource: resources)
				resourceStatus += resource.getClass().getSimpleName()+" "+resource.getName()+" "+resource.getStatus()+", ";
		} else {
			for (RequiredTestResource reqResource: runnableItem.getRequiredTestResources())
				resourceStatus += reqResource.getTestExecResourceClass().getSimpleName()+" "+reqResource.getProperties()+", ";
		}
		
		if (resourceStatus.length()>=2)
			resourceStatus = resourceStatus.substring(0, resourceStatus.length() - 2);

	}

	public void addPerfomanceResult(String operationName, Date date, double delay){
		
		try {
			PerfomanceReport perfReport = suiteExecContext.getPerfomanceReport();
			perfReport.addResult(operationName, date, delay);
		}  catch (Exception e){
			log.info(e,e);			
		}
		
	}
	
	public void addError(String errorText, WarningCaseArtifactsBuilder[] artifactsBuilders) {

		org.kkonoplev.bali.classifyreport.model.Warning warning = WarningBuilder.build(errorText, this, artifactsBuilders);
		suiteExecContext.getClassifyReport().addWarning(warning);
		errorCount++;
		
		ClassifyReportHTMLBuilder classifyReportBuilder = new ClassifyReportHTMLBuilder(suiteExecContext.getClassifyReport());
		classifyReportBuilder.save();

		if (getDebugMode()) {

			TestExecutor testExecutor = getTestExecutor();

			if (testExecutor != null) {
				testExecutor.waitOnMonitor();
			}

		}
	}

	public void addCaptureEvent(String captureText, WarningCaseArtifactsBuilder[] artifactsBuilders) {

		String msg = runnableNode.getPath()+"node "+this.getSuiteExecContext().getNodeId()+", case "+this.threadId+" ,, "+captureText;
		Warning warning = WarningBuilder.build(msg, this, artifactsBuilders);
		suiteExecContext.getCaptureReport().addWarning(warning);
		
		ClassifyReportHTMLBuilder classifyReportBuilder = new ClassifyReportHTMLBuilder(ClassifyReportViewConfig.CAPTURE, suiteExecContext.getCaptureReport());
		classifyReportBuilder.save();

	}

		
	/*
	 * initialize context
	 */
	public void initContext() {

		status = TestExecContextState.INIT_CONTEXT;

		log.info("Init all resources");
		for (TestExecResource resource : resources) {
			try {
				resource.init(this);
			} catch (Exception e){
				log.warn(e,e);
				throw new RuntimeException("Excetion " + e
							+ " while init resource " + resource+" for context "+this);
			}
		}

	}
	
	public boolean preLockRequiredResources() {
		log.info("Try to pre-lock all required resources");
		ArrayList<TestExecResource> preLockedResources = new ArrayList<TestExecResource>(); 
		try {
			
			for (RequiredTestResource reqTestResource : runnableItem.getRequiredTestResources()){
				TestExecResource resource = getProjectService().getFreeResource(reqTestResource, this);
				if (resource == null) {
					log.info(reqTestResource.toString()+" is missing in resource pool!");
					unlockPreLockedResources(preLockedResources);
					return false;
				}
				
				log.info("Resource "+resource.toString()+" pre-locked");
				resource.setPreLocked(true);
				preLockedResources.add(resource);
			}
			
			setResources(preLockedResources);
			return true;
			
		} catch (Throwable e) {
			log.error(e, e);
			unlockPreLockedResources(preLockedResources);
			return false;
		}
		
	}

	
	private void unlockPreLockedResources(
			ArrayList<TestExecResource> preLockedResources) {

		log.info("Unlock all pre-locked resources, cant find all required!");
		for (TestExecResource testExecResource: preLockedResources)
			testExecResource.setPreLocked(false);

	}

	public TestExecResource getResource(RequiredTestResource reqTestResource) {
		return getResource(reqTestResource, 1);
	}
	
	public TestExecResource getResource(RequiredTestResource reqTestResource, int order) {
		
		int curOrder = 0;
		for (TestExecResource resource: resources){
			if (resource.getClass().equals(reqTestResource.getTestExecResourceClass()) && resource.matchRequirements(reqTestResource.getProperties()) ){
				curOrder++;
				if (curOrder == order)
					return resource;
			}
		}
		
		throw new RuntimeException("Can't find "+order+" instance of resource "+reqTestResource.toString()+" in allocated pool.");
		
	}
	
	/*
	 * before calling this function we ensure there are free resources
	 */
	public void lockResources() {
		log.info("Locking pre-locked resources");
		for (TestExecResource testExecResource: resources){
			log.info(testExecResource + " is LOCKED by " + this);
			testExecResource.setTestExecContext(this);
		}
		
	}

	public void unlockResources() {
		log.info("UNLOCKing pre-locked resources");
		for (TestExecResource testExecResource: resources){
			log.info(testExecResource + " is UN-LOCKED by " + this);
			testExecResource.setTestExecContext(null);
			testExecResource.setPreLocked(false);
		}
		resources.clear();
	}


	public String getResourcesNames() {
		updateResourceStatus();
		return resourceStatus;
	}

	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getClassName() {
		return className;
	}

	public String getShortClassName() {
		return className.replace(testClassPre, "");
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public SuiteExecContext getSuiteExecContext() {
		return suiteExecContext;
	}

	public void setSuiteExecContext(SuiteExecContext suiteExecContext) {
		this.suiteExecContext = suiteExecContext;
	}

	public Date getStartDate() {
		return startDate;
	}

	public String getStartDateFmt() {
		return DateUtil.dateFormat(fmt, startDate);
	}

	public String getEndDateFmt() {
		return DateUtil.dateFormat(fmt, endDate);
	}

	public boolean getCaptureMode() {
		return captureMode;
	}

	public void setCaptureMode(boolean captureMode) {
		this.captureMode = captureMode;
	}

	public double getElapsed() {

		if (startDate == null)
			return 0;

		long start = startDate.getTime();
		long now;

		if (endDate != null)
			now = endDate.getTime();
		else
			now = new Date().getTime();

		double diff = now - start;
		if (diff > 0)
			diff = diff / 60000;

		return diff;

	}

	public String getElapsedFmt() {

		DecimalFormat myFormatter = new DecimalFormat("###.##");
		String output = myFormatter.format(getElapsed());

		return output;

	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isNotDetached() {

		if (startDate == null)
			return true;
		else
			return false;

	}

	public File getPackageDir() {
		File packageDir = new File(suiteExecContext.getResultDirFile(),
				this.className
						.replaceAll("\\.", "\\" + File.separator));
		return packageDir;
	}

	public boolean getDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public boolean isFinished() {

		if (endDate == null)
			return false;
		else
			return true;
	}

	public String toString() {
		return suiteExecContext.getSuiteMdl().getName() + "-" + runnableNode.getLineViewLabel() + " "
				+ status + " started:" + startDate + " end:" + endDate;
	}

	public void detach() {

		startDate = new Date();
		status = TestExecContextState.DETACHED;
		log.info("Job: " + this + " detached on " + startDate);

	}

	public void done() {

		if (getErrorCount() > 0)
			status = TestExecContextState.FAIL;
		else
			status = TestExecContextState.SUCC;

		onFinished();

	}

	public void onFinished() {
		endDate = new Date();
		log.info("TestExecContext finished: " + this);
		testExecutor = null;
		suiteExecContext.getExecProcessor().onTestJobFinished(this);
	}

	public void cancel() {
		startDate = new Date();
		status = TestExecContextState.CANCELED;
		onFinished();
	}

	public void runtimeError(Throwable e) {
		status = TestExecContextState.RUNERROR;
		onFinished();
	}

	public boolean getCompleted() {
		return (endDate != null);
	}



	public TestExecContextState getStatus() {
		return status;
	}

	public void setStatus(TestExecContextState status) {
		this.status = status;
	}
	


	/**
	 * Getting error count occurs during test execution.
	 * 
	 * @return error count.
	 */
	public synchronized int getErrorCount() {
		return errorCount;
	}

	public TestExecutor getTestExecutor() {
		return testExecutor;
	}

	public void setTestExecutor(TestExecutor testExecutor) {
		this.testExecutor = testExecutor;
	}


	public void start() {
		if (startDate == null)
			startDate = new Date();
		status = TestExecContextState.STARTED;
	}

	public ProjectService getProjectService() {
		return getSuiteExecContext().getExecProcessor().getProjectSvc();
	}

		
	

	public void interrupt() {
		if (this.isNotDetached()) {
			startDate = new Date();
			endDate = new Date();
			status = TestExecContextState.INTERRUPPED;
		}

		if (!this.isFinished()) {
			endDate = new Date();
			status = TestExecContextState.INTERRUPPED;
		}
		this.onFinished();
	}


	public TreeNode getRunnableNode() {
		return runnableNode;
	}

	public RunnableItem getRunnableItem() {
		return runnableItem;
	}

	public int getLoops() {
		return loops;
	}

	public void setLoops(int loops) {
		this.loops = loops;
	}

	public ArrayList<TestExecResource> getResources() {
		return resources;
	}

	public void setResources(ArrayList<TestExecResource> resources) {
		this.resources = resources;
	}

	public Message getExportResultMessage() {
		return exportResultMessage;
	}

	public void setExportResultMessage(Message exportResultMessage) {
		this.exportResultMessage = exportResultMessage;
	}


	public boolean getCallBackOld() {
		
		if (exportResultMessage.getDate() == null)
			return false;
		
		if (endDate == null)
			return true;
		
		if (exportResultMessage.getDate().before(endDate))
			return true;
		
		return false;
		
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	public void setRunnableNode(TreeNode runnableNode) {
		this.runnableNode = runnableNode;
	}

	public void setRunnableItem(RunnableItem runnableItem) {
		this.runnableItem = runnableItem;
	}

	public String getResourceStatus() {
		updateResourceStatus();
		return resourceStatus;
	}

	public void setResourceStatus(String resourceStatus) {
		this.resourceStatus = resourceStatus;
	}

	public boolean getNeedStop() {
		return needStop;
	}

	public void setNeedStop(boolean needStop) {
		this.needStop = needStop;
	}
	
	
	
	
}
