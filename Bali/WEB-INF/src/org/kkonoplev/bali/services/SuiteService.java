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

package org.kkonoplev.bali.services;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.init.BaliProperties;
import org.kkonoplev.bali.suiteexec.RerunOnNewEnvsInfo;
import org.kkonoplev.bali.suiteexec.SuiteExecContext;
import org.kkonoplev.bali.suiteexec.SuiteExecProcessor;
import org.kkonoplev.bali.suiteexec.SuiteMdl;
import org.kkonoplev.bali.suiteexec.TestExecContext;
import org.kkonoplev.bali.suiteexec.TestExecContextBuilder;

public class SuiteService {
		
	SuiteExecProcessor suiteExecProcessor;	

	private static final Logger log = Logger.getLogger(SuiteService.class);

	public enum SuiteTag {
		NAME("name"),
		DESC("description"),
		BROWSER("browser"),
		EMAIL("email"),
		CRON("cron"),
		OPTIONS("options"),
		TESTS("tests"),
		RUMPUP("rumpup"),	
		LOADMINUTES("loadminutes"),	
		LOADMODE("loadmode"),	
		DEBUGMODE("debugmode"),
		CAPTUREMODE("capturemode");
		
		String name;		
		
		private SuiteTag(String name_){
			name = name_;
		}
			
		public String getName(){
			return name;
		}
		
	};
	
	public SuiteService() throws Exception{
		suiteExecProcessor = new SuiteExecProcessor(BaliServices.getProjectService(),
				BaliProperties.getTestExecutorCount());		

	}	
	
	public SuiteExecProcessor getSuiteExecProcessor() {
		return suiteExecProcessor;
	}

	public void setSuiteExecProcessor(SuiteExecProcessor suiteExecProcessor) {
		this.suiteExecProcessor = suiteExecProcessor;
	}

	public void loadDefaultMdl(SuiteMdl suiteMdl) {
		
		String filename = suiteMdl.getFilename();		
		System.out.println("Name:"+filename);
		System.out.println("Filelist length:"+getSuitesFileList().length);
		
		if (filename.length() >0){
			loadSuiteMdlFromFile(BaliProperties.getBaliSuitesDir()+File.separator+filename, suiteMdl);						
		} else			
		if (getSuitesFileList().length > 0) {		
			filename = getSuitesFileList()[0].getAbsolutePath();					
			loadSuiteMdlFromFile(filename, suiteMdl);				
		}
		
	}
	

	public Properties loadSuiteProperties(SuiteMdl suiteMdl) throws Exception{
		
		Properties properties = new Properties();
		if (suiteMdl.getOptions().equals(""))
			return properties;
		
		FileReader freader = new FileReader(BaliProperties.getBaliOptionsDir()+suiteMdl.getOptions()+".properties");		
		properties.load(freader);
		
		return properties;
		
	}
	
	public synchronized void loadSuiteMdlFromFile(String file, SuiteMdl suiteMdl){		
	
		log.info("Load suite mdl from file: "+file);
		File srcFile = new File(file);		
		loadSuiteMdlFromFile(srcFile, suiteMdl);		
			
	}
	
	
	
	public synchronized void loadSuiteMdlFromFile(File srcFile, SuiteMdl suiteMdl){
		 	
		try {
			Properties props = new Properties();
			FileReader fr = new FileReader(srcFile);
			props.load(fr);
			
			suiteMdl.setName(props.getProperty(SuiteTag.NAME.getName()));
			suiteMdl.setDescription(props.getProperty(SuiteTag.DESC.getName()));
			suiteMdl.setBrowser(props.getProperty(SuiteTag.BROWSER.getName()));
			suiteMdl.setEmail(props.getProperty(SuiteTag.EMAIL.getName()));
			suiteMdl.setCron(props.getProperty(SuiteTag.CRON.getName()));
			suiteMdl.setOptions(props.getProperty(SuiteTag.OPTIONS.getName()));
			suiteMdl.setTests(props.getProperty(SuiteTag.TESTS.getName()));
			suiteMdl.setRumpup(props.getProperty(SuiteTag.RUMPUP.getName()));
			suiteMdl.setLoadminutes(props.getProperty(SuiteTag.LOADMINUTES.getName()));
			suiteMdl.setLoadmode(props.getProperty(SuiteTag.LOADMODE.getName()).equals("true") ? true: false);
			suiteMdl.setDebugmode(props.getProperty(SuiteTag.DEBUGMODE.getName()).equals("true") ? true: false);
			suiteMdl.setCapturemode(props.getProperty(SuiteTag.CAPTUREMODE.getName()).equals("true") ? true: false);
			suiteMdl.setFilename(srcFile.getName());			
			
			fr.close();
		} catch (Exception e){
			e.printStackTrace();
		}
			
	}


	public synchronized File[] getSuitesFileList(){
		
		File f = new File(BaliProperties.getBaliSuitesDir());	
		File[] files = f.listFiles();
		return files;
		
	}

	public synchronized void saveSuite(SuiteMdl suiteMdl) {
		
		suiteMdl.setFilename(suiteMdl.getName()+".conf");
		saveSuite(BaliProperties.getBaliSuitesDir()+File.separator+suiteMdl.getFilename(), suiteMdl);
		
	}
		
	public void saveSuite(String path, SuiteMdl suiteMdl) {
		
		FileWriter fw;
		try {
			
			log.info("saving suite to "+path);
			
			fw = new FileWriter(path);
		
			fw.write(SuiteTag.NAME.getName()+"="+suiteMdl.getName()+"\n");
			fw.write(SuiteTag.DESC.getName()+"="+suiteMdl.getDescription()+"\n");
			fw.write(SuiteTag.CRON.getName()+"="+suiteMdl.getCron()+"\n");
			fw.write(SuiteTag.BROWSER.getName()+"="+suiteMdl.getBrowser()+"\n");
			fw.write(SuiteTag.EMAIL.getName()+"="+suiteMdl.getEmail()+"\n");
			fw.write(SuiteTag.OPTIONS.getName()+"="+suiteMdl.getOptions()+"\n");
			fw.write(SuiteTag.LOADMODE.getName()+"="+suiteMdl.getLoadmode()+"\n");
			fw.write(SuiteTag.RUMPUP.getName()+"="+suiteMdl.getRumpup()+"\n");
			fw.write(SuiteTag.LOADMINUTES.getName()+"="+suiteMdl.getLoadminutes()+"\n");
			fw.write(SuiteTag.DEBUGMODE.getName()+"="+suiteMdl.isDebugmode()+"\n");
			fw.write(SuiteTag.CAPTUREMODE.getName()+"="+suiteMdl.isCapturemode()+"\n");
			fw.write(SuiteTag.TESTS.getName()+"="+suiteMdl.getTests()+"\n");
				
			fw.flush();
			fw.close();

		} catch (IOException e) {
			log.info(e);
		}		
		
	}

	public synchronized void deleteSuite(SuiteMdl suiteMdl) {

		File f = new File(BaliProperties.getBaliSuitesDir()+File.separator+suiteMdl.getFilename());
		
		if (f.exists()){
			f.delete();
			
			log.info("DELETE "+f.getAbsolutePath());
		} else {
			log.info("NOT FOUND "+f.getAbsolutePath());
			
		}
		
	}
	
	public void rerunOnNewEnvironments(RerunOnNewEnvsInfo rerunOnNewEnvsInfo) throws Exception {
		
		SuiteExecContext suiteExecContext = getSuiteExecContext(rerunOnNewEnvsInfo.getResultDir());
		SuiteMdl suiteMdl = suiteExecContext.getSuiteMdl();
		SuiteMdl editedSuiteMdl = (SuiteMdl) suiteMdl.clone();
		
		ArrayList<String> rerunTestClasses = suiteExecProcessor.calcRerunTestClasses(suiteExecContext.getClassifyReport(), rerunOnNewEnvsInfo.getWarningIDs());
		String newTests = suiteExecProcessor.filterTests(editedSuiteMdl.getTestList(), rerunTestClasses);
		editedSuiteMdl.setTests(newTests);
		
		for (String envName: rerunOnNewEnvsInfo.getEnvironments()){
			SuiteMdl envSuiteMdl = editedSuiteMdl.clone();
			envSuiteMdl.setOptions(envName);
			runSuite(envSuiteMdl);
		}
		
	}


	public SuiteExecContext runSuite(SuiteMdl suiteMdl) throws Exception {			
		
		Map<String, String> environments = BaliProperties.getOptionsList(BaliProperties.getBaliOptionsDir());
		SuiteExecContext execContext =  suiteExecProcessor.addSuiteToJobQueue(suiteMdl, BaliProperties.getBaliResultsDir(), BaliProperties.getBaliOptionsDir(), environments);
		String rumpup = execContext.getSuiteMdl().getRumpup();
		int rumpupms = Integer.valueOf(rumpup);
		suiteExecProcessor.notifyExecutors(rumpupms);
		saveSuite(BaliProperties.getBaliResultsDir()+File.separator+execContext.getResultDir()+File.separator+"suite.conf", suiteMdl);
		return execContext;
		
	}
	
	public  void stopSuite(String dir) throws Exception {			
		
		suiteExecProcessor.cancelExecution(dir);		
	}
	
	
	

	public synchronized Map<String, String> getSuitesList(){
		
		File f = new File(BaliProperties.getBaliSuitesDir());	
		File[] files = f.listFiles();
		
		Map<String, String> list = new LinkedHashMap<String, String>();
		for (File fl: files)
			list.put(fl.getName(), fl.getName().replace(".conf",""));

		return list;
		
	}

	
	public static Map getHistoryList(){	
		
		File f = new File(BaliProperties.getBaliResultsDir());	
		
		Map<String, String> list = new LinkedHashMap<String, String>(100);		
		File[] files = f.listFiles();
		
		
		//sort by date
		Arrays.sort(files, new Comparator<File>(){
		    public int compare(File f1, File f2)
		    {
		        return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
		    } });

		
		for (File fl: files)
			list.put(fl.getName(), fl.getName().replaceAll("_"," "));		
		
		return list;	
		
	}
	
	public static File[] getHistory(){
		
		File f = new File(BaliProperties.getBaliResultsDir());	
		File[] files = f.listFiles();
		
		//sort by date
		Arrays.sort(files, new Comparator<File>(){
		    public int compare(File f1, File f2)
		    {
		        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
		    } });
				
		return files;
		
	}

	public void loadMdlFromRunDir(SuiteMdl suiteMdl) {
		
		File rundirMdl = new File(BaliProperties.getBaliResultsDir()+File.separator+suiteMdl.getResultDir()+File.separator+"suite.conf");
		loadSuiteMdlFromFile(rundirMdl, suiteMdl);	
		
	}

	public SuiteExecContext getSuiteExecContext(String dir) throws IOException, ClassNotFoundException {
		
		
		// if in active suites
		SuiteExecContext contx = suiteExecProcessor.findSuiteExecContext(dir);
		
		if (contx != null)
			return contx;
			
		log.info("Loading suite ExecContext from filesystem");
		contx = new SuiteExecContext();
		String filepath = BaliProperties.getBaliResultsDir()+File.separator+dir+File.separator+SuiteExecContext.savefile;
		
		contx = SuiteExecContext.deserialize(filepath);
		
		return contx;		
	}

	public void dinit() {
		suiteExecProcessor.dinit();
		
	}

	public HashMap<String, String> getLastTenSuiteContexts() {
		
		File[] f = getHistory();

		HashMap<String, String> s = new LinkedHashMap<String, String>(30);
		
		for (SuiteExecContext execContext: suiteExecProcessor.getSuiteExecContexts()){
			s.put(execContext.getResultDir(), "active");
		}
		
		for (int i = f.length-1; i >=0 && f.length-i <= 15; i--){
			if (s.get(f[i].getName()) == null){
				s.put(f[i].getName(), "done");
			}
		}
		
		return s;
	}

	public void deleteSuiteContext(SuiteExecContext suiteExecContext) {
		String ctxpath = BaliProperties.getBaliResultsDir()+File.separator+suiteExecContext.getResultDir();
		File ctxDir = new File(ctxpath);
		log.info("Delete SuiteExecContext: "+ctxDir.getAbsolutePath());
		ctxDir.delete();		
	}

	public void addNewThreads(SuiteExecContext ctx, int threadCount) {
		
		TestExecContextBuilder testContextBuilder = new TestExecContextBuilder();
		
		int size = ctx.getTestExecContexts().size();
		TestExecContext lastContext = ctx.getTestExecContexts().get(size-1);

		for (int i = 1; i <= threadCount; i++)
			ctx.getTestExecContexts().add(testContextBuilder.build(ctx, lastContext.getProjectName(), lastContext.getClassName(), lastContext.getThreadId()+i));
		
	}
	


	
	
	


	
}
