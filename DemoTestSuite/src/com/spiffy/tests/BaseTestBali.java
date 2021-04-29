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

package com.spiffy.tests;


import com.spiffy.framework.utils.DocServiceBrowser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.IFrameArtifactsBuilder;
import org.kkonoplev.bali.classifyreport.TestLogArtifactsBuilder;
import org.kkonoplev.bali.classifyreport.WarningCaseArtifactsBuilder;
import org.kkonoplev.bali.common.logger.LocalRunLogger;
import org.kkonoplev.bali.selenium.browser.Browser;
import org.kkonoplev.bali.selenium.browser.BrowserArtifactsBuilder;
import org.kkonoplev.bali.selenium.browser.MobileArtifactsBuilder;
import org.kkonoplev.bali.suiteexec.LocalRunBaliContext;
import org.kkonoplev.bali.suiteexec.SoftStopException;
import org.kkonoplev.bali.suiteexec.TestExecContext;
import org.kkonoplev.bali.suiteexec.TestExecContextThreadLocal;
import org.kkonoplev.bali.suiteexec.annotation.UseResources;
import org.kkonoplev.bali.suiteexec.resource.EnvResource;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import ru.kerala.client.QALog;

import java.util.Date;
import java.util.Properties;

public class BaseTestBali extends junit.framework.TestCase {

	public final static String BROWSER_TYPE = Browser.BROWSER_TYPE.CHROME.getType();
	public static final String LOCAL_ENV = "DEV";
			
	protected static final Logger log = LogManager.getLogger(BaseTestBali.class);
	
	@Override
	public void setUp() throws Exception {		
		initTestExecContextOnLocalRun(this.getClass());
        log.info("Begin of testcase [" + getName() + "].");

		QALog.initScenarioProject(this.getClass().getSimpleName(), "1", "spiffy");
		QALog.configureServerURL("http://localhost:8080");
    }
	
	public static void checkSoftStop() throws SoftStopException{
		
		TestExecContext testExecContext = getTestExecContext();
		
		if (testExecContext.getNeedStop())
			throw new SoftStopException();
		
	}
	
	/*
	 * init TestExecContext and other environment if test is run locally from Eclipse or other IDE/command line
	 */
	public void initTestExecContextOnLocalRun(Class testClass) throws Exception{
		
		// check if TestExecContext is defined for ThreadLocal var
		TestExecContext testExecContext = getTestExecContext();
		if (testExecContext != null)
			return;
		
		
		// need to init loggers and TestExecContext
		LocalRunLogger.initThreadsAndStdOutLogger();
		log.info("Check text exec context in thread: "+Thread.currentThread().getName());
		
		// if test is not started from Bali - then suite/testExecContext is not initialized.
		if (testExecContext == null){

			System.setProperty("phantomjs.binary.path", "C:\\selenium\\phantomjs.exe");
			System.setProperty("webdriver.ie.driver", "C:\\selenium\\IEDriverServer.exe");
			System.setProperty("webdriver.chrome.driver", "C:\\selenium\\chromedriver.exe");
			System.setProperty("webdriver.firefox.profile", "default");
		
			LocalRunBaliContext localBaliRunContext = new LocalRunBaliContext();
			
			String resultOutputDir = (System.getProperty("test.report.dir") != null) ? System.getProperty("test.report.dir") : "target/report/";
			localBaliRunContext.setResultDir(resultOutputDir);
			localBaliRunContext.setPropertiesDir("conf/");
			localBaliRunContext.setOptions(LOCAL_ENV);
			localBaliRunContext.setCaptureMode(true);			
			localBaliRunContext.setLocalClass(testClass.getCanonicalName());
			
			// add here resources You plan to get using getResources(...) API
			// set browser type, default - CHROME
			String browserType = System.getProperty("browser") != null ? System.getProperty("browser") : BROWSER_TYPE;
			localBaliRunContext.setBrowserType(browserType);
			localBaliRunContext.getLocalResources().add(new Browser(browserType,""));
			localBaliRunContext.getLocalResources().add(new EnvResource("Desktop", EnvResource.ALLENV));

			Bali.activateLocalBaliRunContext(localBaliRunContext);
		}
		
		//init threads logger file after test exec conext is initialized and its className is defined
		TestExecContextThreadLocal.getTestExecContext().setCurrentThreadLogAppendersFile();

		
			
	}
	
	
	@Override
	public void tearDown(){
		log.info("End of testcase " + "[" + getName() + "].");		

		if (TestExecContextThreadLocal.getTestExecContext().getErrorCount() == 0)
			log.info("TEST IS PASSED.");
		else
			log.info("TEST IS FAILED. HAS "+TestExecContextThreadLocal.getTestExecContext().getErrorCount()+" errors." );
			
			
	}
	
	public static TestExecContext getTestExecContext(){
		return TestExecContextThreadLocal.getTestExecContext();
	}
	
	public static DocServiceBrowser docServiceBrowser = new DocServiceBrowser();
	
	public static void addNewTestStepBrowser(String step){
		QALog.addNewTestStep(step, docServiceBrowser);
	
	}

	
	public static Properties  getProperties(){
		return TestExecContextThreadLocal.getTestExecContext().getSuiteExecContext().getProperties();
	}

	@Override
	protected void runTest() throws Throwable {
	        try {
	            super.runTest();
	        } catch (Throwable t) {

	        	log.info(t.toString());
	        	t.printStackTrace();
	        	
	        	if (t instanceof WebDriverException && t.toString().contains("is not clickable at point")){
	        		int length = t.toString().length();
	        		Results.addUIError("Element is not clickable at point ,, "+t.toString().substring(71, length > 100 ? (100-71) : (length-71)));
	        	} else
	        	if (t instanceof WebDriverException && t.toString().contains("Element is not clickable")){
	        		int length = t.toString().length();
	        		Results.addUIError("Element is not clickable ,, "+t.toString().substring(71, length > 150 ? (150-71) : (length-71)));
	        	} else
	        	if (t instanceof org.openqa.selenium.ElementNotVisibleException){
	        		int length = t.toString().length();
	        		Results.addUIError("selenium.ElementNotVisibleException ,, "+t.toString().substring(50, length > 150 ? (150-51) : (length-51)));
	        	} else
	        	if (t instanceof org.openqa.selenium.NoSuchElementException){
	        		int length = t.toString().length();
	        		Results.addUIError("selenium.NoSuchElementException ,, "+t.toString().substring(44, length > 150 ? (150-45) : (length-45)));
	        	} else {
	        		Results.addUIError(t.toString());
	        	}
				throw t;
	        }
	 }
	 

	

	
	 



	
}