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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.Thread.State;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.htmlbuilder.ClassifyReportHTMLBuilder;
import org.kkonoplev.bali.classifyreport.model.ClassifyReport;
import org.kkonoplev.bali.common.perfomance.PerfomanceReport;
import org.kkonoplev.bali.common.utils.DateUtil;
import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.services.ProjectService;


public class SuiteExecContext implements Serializable {
	
	private static final Logger log = Logger.getLogger(SuiteExecContext.class);
	
	private transient SuiteExecProcessor execProcessor;
	private ArrayList<TestExecContext> testExecContexts;
	
	private SuiteMdl suiteMdl;
	
	private String exporterMetaData = "";
	
	private boolean loadMode;
	private int loadminutes = 0;
	private String rumpup = "3000";

	private boolean underExporting = false;
	private Message exportResultMessage = new Message();
	
	private File resultDirFile;
	private String resultDir;
	private Properties properties;
	private Date startDate;
	private Date endDate;
	
	
	private ClassifyReport classifyReport, captureReport;
	private PerfomanceReport perfomanceReport;
	
	
	private static final String fmt = "MM/dd HH:mm:ss";
	
	public SuiteExecContext(){
		
	}

	public SuiteExecContext(SuiteExecProcessor execProcessor){
		this.execProcessor = execProcessor;	
	}
	
	public void softStop() {
		
		log.info("Set Soft Stop Flags " +this);
		
		for (TestExecContext testJob: testExecContexts){
			
			if (testJob.isFinished())
				continue;
			
			if (testJob.isNotDetached())
				testJob.cancel();
			else 
				testJob.setNeedStop(true);
			
		}
		
	}

		
	public void cancel() {
		log.info("Cancel run " +this);
		
		for (TestExecContext testJob: testExecContexts){
			
			if (testJob.isFinished())
				continue;
			
			if (testJob.isNotDetached()) 
				testJob.cancel();
			else {
				
				try {
					TestExecutor executor = testJob.getTestExecutor();
					testJob.setStatus(TestExecContextState.THREAD_STOP);
					if (executor != null){
						//executor.interrupt();
						executor.stop();
						Thread.sleep(200);
						if (executor.getState() == State.TERMINATED){
							testJob.setStatus(TestExecContextState.TERMINATED);
							testJob.onFinished();
							execProcessor.replaceWithNew(executor.getName());
						}
					
					}
				} catch (Exception e){
					log.error(e, e);
				}
			}
		}
		
	}
	
	public void serialize(File file){
		
		try {
			//log.info(toString()+" serializing to file "+file.getAbsolutePath());
		
			OutputStream fr = new BufferedOutputStream(new FileOutputStream(file));
			ObjectOutputStream oos = new ObjectOutputStream(fr);
			oos.writeObject(this);
			oos.flush();
			oos.close();
		} catch (Throwable t){
			log.warn("Error when serialing to file"+file.getAbsolutePath());
			log.warn(t,t);
		}
		
		//log.info(toString()+" completed serializing to file "+file.getAbsolutePath());		
		
	}
	
	public static SuiteExecContext deserialize(String string) throws IOException, ClassNotFoundException{
		
		//log.info("Deserialize SuiteExecContext from file: "+string);
		InputStream is = new BufferedInputStream(new FileInputStream(string));
		ObjectInputStream ois = new ObjectInputStream(is);
		SuiteExecContext suiteExecContext = (SuiteExecContext)ois.readObject();
		
		//log.info(suiteExecContext+" was successfully deserialized. ");

		return suiteExecContext;		
	}
	
	public void save() {
		File saveFile = new File(resultDirFile, savefile);
		serialize(saveFile);
	}
	

	public void update(TestExecContext testExecContext_) {
		
		TestExecContext testExecContext = findByClassName(testExecContext_.getClassName());
		if (testExecContext == null)
			return;
		
	}

	public TestExecContext findByClassNameThreadId(String className, int threadId) {

		for (TestExecContext testExecContext: testExecContexts){
			if (testExecContext.getClassName().equals(className) &&	testExecContext.getThreadId() == threadId)
				return testExecContext;
		}
		
		return null;
	}

	public TestExecContext findByClassName(String className) {

		for (TestExecContext testExecContext: testExecContexts){
			if (testExecContext.getClassName().equals(className))
				return testExecContext;
		}
		return null;
	}
	
	public void dinit() {
	
		for (TestExecContext testExecContext: testExecContexts){
			if (testExecContext.isFinished())
				continue;
			
			testExecContext.interrupt();		
		}
		
	}


	public Properties getProperties() {
		return properties;
	}
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public SuiteExecProcessor getExecProcessor() {
		return execProcessor;
	}

	public void setExecProcessor(SuiteExecProcessor execProcessor) {
		this.execProcessor = execProcessor;
	}

	
	public TestExecContext getNextFreeJobWithPreLockedResources(){
		log.info("Suite "+resultDir+" getNextFreeJobWithPreLockedResources ");
		for (TestExecContext testExecContext: testExecContexts){
			if (testExecContext.isNotDetached() && testExecContext.preLockRequiredResources()){
				return testExecContext;			
			}			
		}
		// TODO remove null
		return null;		
	}
	
	
	public ArrayList<TestExecContext> getTestExecContexts() {
		return testExecContexts;
	}

	public void setTestJobs(ArrayList<TestExecContext> testJobs) {
		this.testExecContexts = testJobs;
	}

	public File getResultDirFile() {
		return resultDirFile;
	}

	public void setResultDirFile(File resultDirFile) {
		this.resultDirFile = resultDirFile;
	}
	
	

	public boolean getLoadmode() {
		return loadMode;
	}
	public void setLoadmode(boolean loadMode) {
		this.loadMode = loadMode;
	}

	
	public int getLoadminutes() {
		return loadminutes;
	}
	public void setLoadminutes(int loadminutes) {
		this.loadminutes = loadminutes;
	}
	
	public String getRumpup() {
		return rumpup;
	}

	public void setRumpup(String rumpup) {
		this.rumpup = rumpup;
	}

	public int getSuccessCount(){
		return getCount(TestExecContextState.SUCC);
	}
	
	public int getFailCount(){
		return getCount(TestExecContextState.FAIL)+getCount(TestExecContextState.RUNERROR);
	}
	
	public int getExecutedCount(){
		return getCount(TestExecContextState.FAIL)+getCount(TestExecContextState.RUNERROR)+getCount(TestExecContextState.SUCC);
	}

	
	public int getTotalCount(){
		return testExecContexts.size();
	}
	
	public double getSuccessRate(){
		
		int total = getExecutedCount();
		int success = getSuccessCount();
		
		if (total == 0)
			return 0;
		
		double r = ((double)success/total)*100;
		
		return r;		
	}

	
	public String getSuccessRateFmt(){
		
		DecimalFormat myFormatter = new DecimalFormat("###");
		String output = myFormatter.format(getSuccessRate());
		
		return output;
				
	}
	
	
	protected int getCount(TestExecContextState status){
		
		int q = 0;
		for (TestExecContext context: testExecContexts)
			if (context.getStatus().equals(status))
				q++;
		
		return q;
			
	}
	
	public int getErrorCount(){
		int q = 0;
		for (TestExecContext context: testExecContexts)
				q+=context.getErrorCount();
		return q;
	}
	
	public int getUniqErrorCount(){
		return classifyReport.getWarnList().size();
	}
	
		
	public Date getStartDate() {
		return startDate;
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
	
	public String getStartDateFmt() {
		return DateUtil.dateFormat(fmt, startDate);
	}
	
	public String getEndDateFmt() {
		return DateUtil.dateFormat(fmt, endDate);
	}
	
	public double getElapsed(){
		
		if (startDate == null)
			return 0;
		
		long start =  startDate.getTime();;
		long now;
		
		if (endDate != null)
			now = endDate.getTime();
		else
			now = new Date().getTime();

		
		double diff = now - start;
		if (diff > 0)
			diff = diff/60000;
			
		return diff;
		
	}
	
	public String getElapsedFmt(){		
		return formatted(getElapsed());
	}
	
	public double getSummaryElapsed(){		
		double s=0;
		for (TestExecContext testExecContext: testExecContexts)
			s += testExecContext.getElapsed();
		return s;		
	}
	
	public double getTimeAccelerateRate(){
		
		double suiteElapsed = getElapsed();
		double summaryElapsed = getSummaryElapsed();
		
		if(suiteElapsed == 0)
			return 1;
		
		double ratio =  summaryElapsed/suiteElapsed;
		
		return ratio;		
		
	}
	
	public String getTimeAccelerateRateFmt(){
		return formatted(getTimeAccelerateRate());
	}
	
	public String formatted(double d){
		
		DecimalFormat myFormatter = new DecimalFormat("###.##");
		String output = myFormatter.format(d);
		
		return output;	
		
	}
	
	public String getSummaryElapsedFmt(){		
		return formatted(getSummaryElapsed());
	}
	
	

	public String getResultDir() {
		return resultDir;
	}

	public void setResultDir(String resultDir) {
		this.resultDir = resultDir;
	}
	
	

	public SuiteMdl getSuiteMdl() {
		return suiteMdl;
	}

	public void setSuiteMdl(SuiteMdl suiteMdl) {
		this.suiteMdl = suiteMdl;
	}

	public SuiteExecContext(SuiteMdl suiteMdl_){
		suiteMdl = suiteMdl_;
	}
	
	
	
	public String toString(){
		String res = resultDir+"_NULL";
		if (suiteMdl != null)
			res = resultDir+suiteMdl.getOptions();
		return res;
	}
	
	public boolean getCompleted(){		

		if (underExporting)
			return false;
		
		for (TestExecContext testExecContext: testExecContexts)
			if (!testExecContext.getCompleted())
				return false;
		
		return true;
		
	}

	public void onTestJobFinished(TestExecContext testExecContext) {		
		
		if (getCompleted()){
			
			if (endDate == null)
				endDate = new Date();
			
			execProcessor.onSuiteExecFinished(this);
		}
		
		
	}


	public static final String savefile = "suiteExecContext.txt";
	
	
	
	public PerfomanceReport getPerfomanceReport() {
		return perfomanceReport;
	}
	
	
	public void setPerfomanceReport(PerfomanceReport perfomanceReport) {
		this.perfomanceReport = perfomanceReport;
	}
	
	public String getExporterMetaData() {
		return exporterMetaData;
	}
	
	public void setExporterMetaData(String exporterMetaData) {
		this.exporterMetaData = exporterMetaData;
	}
	
	public Message getExportResultMessage() {
		return exportResultMessage;
	}
	
	public void setExportResultMessage(Message exportResultMessage) {
		this.exportResultMessage = exportResultMessage;
	}
	
	public boolean getUnderExporting() {
		return underExporting;
	}
	public void setUnderExporting(boolean underExporting) {
		this.underExporting = underExporting;
	}

	public ClassifyReport getClassifyReport() {
		return classifyReport;
	}

	public ClassifyReport getCaptureReport() {
		return captureReport;
	}

	public void setTestExecContexts(ArrayList<TestExecContext> testExecContexts) {
		this.testExecContexts = testExecContexts;
	}

	public void setClassifyReport(ClassifyReport classifyReport) {
		this.classifyReport = classifyReport;
	}

	public void setCaptureReport(ClassifyReport captureReport) {
		this.captureReport = captureReport;
	}
	
	public int getNodeId(){
		return suiteMdl.getNodeId();
	}
	
	
}
