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

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.htmlbuilder.ClassifyReportHTMLBuilder;
import org.kkonoplev.bali.classifyreport.model.ClassifyReport;
import org.kkonoplev.bali.classifyreport.model.Warning;
import org.kkonoplev.bali.classifyreport.model.WarningCase;
import org.kkonoplev.bali.common.utils.MailUtil;
import org.kkonoplev.bali.services.CommunityEdition;
import org.kkonoplev.bali.services.ProjectService;


public class SuiteExecProcessor {
	
	private static final Logger log = Logger.getLogger(SuiteExecProcessor.class);
	public static int MAX_EXEC_SUITES = 20;
	
	private List<SuiteExecContext> suiteExecContexts = new ArrayList<SuiteExecContext>(MAX_EXEC_SUITES);	
	private ExecProcessorThreadNotifier threadNotifier;
	private ArrayList<TestExecutor> testExecutors;
	private ProjectService projectSvc;
	private int id = 1;
	
	protected String baseURL = "";
	int curPointer = 0;
	
	
	public SuiteExecProcessor(){
	}
	
	public SuiteExecProcessor(ProjectService projectSvc, int execCount) throws Exception{
		
		log.info("Init Suite Exec Processor");
		this.projectSvc = projectSvc;	
		int threadsCount = execCount;
		
		if (CommunityEdition.isEnabled() && execCount > CommunityEdition.MAX_THREADS)
			threadsCount = CommunityEdition.MAX_THREADS;
		
		
		testExecutors = new ArrayList<TestExecutor>(execCount);
		for (int i = 0; i < threadsCount; i++){			
			TestExecutor testExecutor = new TestExecutor(this, id++);
			testExecutors.add(testExecutor);
		}
		
		threadNotifier = new ExecProcessorThreadNotifier(this);		
		threadNotifier.start();
				
	}	
	
		
	public synchronized TestExecContext detachNextFreeJob(){
		
		log.info("Looking for next job with free resources");
		
		for (int i=0; i < suiteExecContexts.size(); i++){
			
			SuiteExecContext suiteExecContext = suiteExecContexts.get((i+curPointer) % suiteExecContexts.size());
			TestExecContext testExecContext = suiteExecContext.getNextFreeJobWithPreLockedResources();
			
			if (testExecContext != null){
				testExecContext.detach();
				testExecContext.lockResources();
				curPointer++;
				return testExecContext;	
			}
			
		}				
		
		return null;		
	}
	
	
	public SuiteExecContext findSuiteExecContext(String dir) {

		for (SuiteExecContext con: suiteExecContexts){
			if (con.getResultDir().equals(dir))
				return con;
		}
		
		return null;		
	}
	
	
	public synchronized List<SuiteExecContext> getSuiteExecContexts() {
		return suiteExecContexts;
	}

	protected void setSuiteExecContexts(List<SuiteExecContext> suiteExecContexts) {
		this.suiteExecContexts = suiteExecContexts;
	}
	

	public synchronized void onSuiteExecFinished(SuiteExecContext execContext) {
		log.info("Suite "+execContext+" execution finished at "+execContext.getEndDate()+".");	
		execContext.save();
		suiteExecContexts.remove(execContext);
		sendMailNotify(execContext);
	}

	
	private void sendMailNotify(SuiteExecContext execContext) {
		String email = execContext.getSuiteMdl().getEmail();
		if (!email.equals("")){
			log.info("Send Mail Notify for "+execContext);
			String desc =  execContext.getSuiteMdl().getDescription(); 
			String url = getBaseURL()+"/form/status/suitecontextemail?resultDir="+execContext.getResultDir();
			log.info("url="+url);
			MailUtil.sendUrlHTML("BaliTestServer@db.com", email, "", "Bali Suite Execution Results "+desc,
					 url, true);
		}
		
	}
	
	

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public synchronized void addSuiteToJobQueque(SuiteExecContext execContext){		
		suiteExecContexts.add(execContext);
		log.info("SuiteExecContext added to processor: "+execContext);		
	}
	
	public synchronized SuiteExecContext addSuiteToJobQueue(SuiteMdl suiteMdl, String dir, String confDir, Map<String, String> environments) throws Exception {
		
		SuiteExecContext execContext = (new SuiteExecContextBuilder()).build(suiteMdl, this, dir, confDir, environments);	
		suiteExecContexts.add(execContext);
		log.info("SuiteExecContext added: "+execContext);

		
		return execContext;
	}
	
	public void notifyExecutors(){
		log.info("Thread notifier started.");
		threadNotifier.pingExecutors(); 		
	}
	
	public void notifyExecutors(int rumpup){
		threadNotifier.setRumpUp(rumpup);
		notifyExecutors();
	}

	public ArrayList<TestExecutor> getTestExecutors() {
		return testExecutors;
	}



	public synchronized void cancelExecution(String resultDir) {
		
		SuiteExecContext suiteExecContext = findSuiteExecContext(resultDir);
		if (suiteExecContext != null)				
			suiteExecContext.cancel();
		 		
	}

	public void softStopExecution(String resultDir) {
	
		SuiteExecContext suiteExecContext = findSuiteExecContext(resultDir);

		if (suiteExecContext != null)				
			suiteExecContext.softStop();
		
	}



	public synchronized void onTestJobFinished(TestExecContext testExecContext)  {
		
		testExecContext.unlockResources();		
		testExecContext.getSuiteExecContext().onTestJobFinished(testExecContext);	
		
	}


	public void dinit() {
		for (SuiteExecContext suiteExecContext: suiteExecContexts){
			suiteExecContext.dinit();
			suiteExecContext.save();
		}
		
	}
	

	public synchronized void rerunWarnings(SuiteExecContext suiteExecContext, String warningsIds) throws Exception {
		
		log.info("Rerun tests from warnings");
		
		if (warningsIds == null)
			return; 
	
		ArrayList<String> rerunTestClasses = calcRerunTestClasses(suiteExecContext.getClassifyReport(), warningsIds);
		clearClassifyReportByWarnIDs(suiteExecContext.getClassifyReport(), warningsIds);
		clearClassifyReportFromTestClasses(suiteExecContext.getClassifyReport(), rerunTestClasses);
		clearClassifyReportByFirstTestClassMatch(suiteExecContext.getCaptureReport(), rerunTestClasses);
		saveClassifyReports(suiteExecContext);
		
		
		if (findSuiteExecContext(suiteExecContext.getResultDir()) == null){

			suiteExecContext.setExecProcessor(this);
			resetTestExecContexts(suiteExecContext, rerunTestClasses);
			addSuiteToJobQueque(suiteExecContext);
					
		} else {
			resetTestExecContexts(suiteExecContext, rerunTestClasses);
		}
		
	}

	public synchronized void rerunTestExecContext(TestExecContext testExecContext) throws Exception {
		
		log.info("Rerun testExecContext "+testExecContext.toString());
		
		SuiteExecContext suiteExecContext = testExecContext.getSuiteExecContext();
			
		clearClassifyReportsFromTestContext(suiteExecContext, testExecContext);
		saveClassifyReports(suiteExecContext);
		

		// if suite is not active on proccessor, means was deserialized at the moment
		if (findSuiteExecContext(suiteExecContext.getResultDir()) == null){
			suiteExecContext.setExecProcessor(this);
			testExecContext.reset();
			addSuiteToJobQueque(suiteExecContext);
		} else {
			testExecContext.reset();
		}
		
		
	}

	private void resetTestExecContexts(SuiteExecContext suiteExecContext,
			ArrayList<String> rerunTestClasses) throws Exception {
		log.info("Reset Test Exec Contexts started.");
		for (String className : rerunTestClasses){
			TestExecContext testExecContext = suiteExecContext.findByClassName(className);
			if (testExecContext != null)
				testExecContext.reset();
			else {
				log.error("Can't find testExecContext for class "+className+" in suite "+suiteExecContext.getResultDir());
			}
		}
		log.info("Reset Test Exec Contexts done.");
	}

	private void clearClassifyReportByWarnIDs(ClassifyReport classifyReport, String warningsIds) {
		
		log.info("clear Classify Report By Warn IDs");
		List<String> warnIDs = Arrays.asList(warningsIds.split(","));
		Iterator it = (Iterator) classifyReport.getWarnList().iterator();
		
		int count = 0;
		while (it.hasNext()){
			Warning warning = (Warning) it.next();
			if (warnIDs.contains(count+""))
				it.remove();
			
			count++;
		}
	}
	
	private void clearClassifyReportFromTestClasses(ClassifyReport classifyReport, ArrayList<String> testClasses) {
		
		log.info("clear Classify Report By First Test Class Match");
		
		Iterator it = classifyReport.getWarnList().iterator();
		while (it.hasNext()){
			Warning warning = (Warning) it.next();
			clearWarningFromTestClasses(warning, testClasses);
			if (warning.getWarningCases().size() == 0)
				it.remove();
		}
	
	}
	
	private void clearWarningFromTestClasses(Warning warning,
		ArrayList<String> testClasses) {
	
		Iterator it = warning.getWarningCases().iterator();
		while (it.hasNext()){
			WarningCase warnCase = (WarningCase) it.next();
			String testClass = warnCase.getTest();
			if (testClasses.contains(testClass))
				it.remove();
		}
		
	}

	private void clearClassifyReportByFirstTestClassMatch(ClassifyReport classifyReport, ArrayList<String> rerunTestClasses) {
		
		log.info("clear Classify Report By First Test Class Match");
		
		Iterator it = classifyReport.getWarnList().iterator();
		while (it.hasNext()){
			Warning warning = (Warning) it.next();
			WarningCase warnCase = warning.getWarningCases().get(0);
			String testClass = warnCase.getTest();
			if (rerunTestClasses.contains(testClass))
				it.remove();
		}
	
	}
	
	public ArrayList<String> calcRerunTestClasses(ClassifyReport classifyReport, String warningsIds) {
		
		log.info("Make rerun list test classes");
		String warnIDs[] = warningsIds.split(",");
		ArrayList<String> rerunTestNames = new ArrayList<String>();
		
		for (String warnId : warnIDs){
			int id = Integer.valueOf(warnId);
			Warning warning = classifyReport.getWarnById(id);
			if (warning == null){
				log.info("No more this warning exists ->"+warning.getMsg());
				continue;
			}
			for (WarningCase warnCase: warning.getWarningCases()){
				if (!rerunTestNames.contains(warnCase.getTest()))
					rerunTestNames.add(warnCase.getTest());
			}
		}

		log.info("items to rerun:"+rerunTestNames.size());
		
		return rerunTestNames;
	}


	
	private void clearClassifyReportsFromTestContext(SuiteExecContext suiteExecContext, TestExecContext testExecContext) throws Exception {
	
		String test = testExecContext.getRunnableNode().getPath();
		suiteExecContext.getClassifyReport().clearFromTest(test);	
		suiteExecContext.getCaptureReport().clearFromTest(test);
		
	}
	
	public void saveClassifyReports(SuiteExecContext suiteExecContext){

		ClassifyReportHTMLBuilder classifyReportBuilder = new ClassifyReportHTMLBuilder(suiteExecContext.getClassifyReport());
		classifyReportBuilder.save();

		classifyReportBuilder = new ClassifyReportHTMLBuilder(suiteExecContext.getCaptureReport());
		classifyReportBuilder.save();
		
	}


	public ProjectService getProjectSvc() {
		return projectSvc;
	}

	public void setProjectSvc(ProjectService projectSvc) {
		this.projectSvc = projectSvc;
	}

	public String filterTests(String[] testList,
			ArrayList<String> rerunTestClasses) {
		
		String tests = "";
		for (String test: testList){
			String[] testDetails = test.split(SuiteMdl.TEST_INFO_DELIM);
			if (rerunTestClasses.contains(testDetails[1]))
				tests += test+",";
		}
		
		if (tests.length() > 0)
			tests = tests.substring(0,  tests.length()-1);
			
		return tests;
	}

	public int cleanHaltedThreads() {
	
		Iterator it = testExecutors.iterator();
		int cleaned = 0;
		while (it.hasNext()){
			TestExecutor exec = (TestExecutor) it.next();
			if (exec.getState() != State.WAITING && exec.getTestExecContext() != null && exec.getTestExecContext().getTestExecutor() == null){
				it.remove();
				cleaned++;
			}
		}
		return cleaned;
			
	}

	public void checkAddThreads(int threadsCount) {
		
		int needThreads = threadsCount - testExecutors.size();
		if (needThreads >= 0){
			for (int i = 0; i < needThreads; i++){
				TestExecutor te = new TestExecutor(this, id++);
				te.start();
				testExecutors.add(te);
			}
		} else {
			
			Iterator it = testExecutors.iterator();
			int cnt = 1;
			while (it.hasNext()){
				it.next();
				if (cnt > threadsCount)
					it.remove();
				cnt++;
			}
			
		}
		
	}

	public boolean replaceWithNew(String name) {
		for (int i = 0; i < testExecutors.size(); i++){
			if (testExecutors.get(i).getName().equals(name)){
				TestExecutor executor = new TestExecutor(this, name);
				testExecutors.set(i, executor);
				return true;
			}
		}
		return false;
	}
	
	
}
