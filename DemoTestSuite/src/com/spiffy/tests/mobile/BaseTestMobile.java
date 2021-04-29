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

package com.spiffy.tests.mobile;

import com.spiffy.framework.utils.Driver;
import com.spiffy.tests.Bali;
import com.spiffy.tests.BaseTestBali;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.kkonoplev.bali.common.logger.LocalRunLogger;
import org.kkonoplev.bali.selenium.browser.Browser;
import org.kkonoplev.bali.suiteexec.LocalRunBaliContext;
import org.kkonoplev.bali.suiteexec.TestExecContext;
import org.kkonoplev.bali.suiteexec.TestExecContextThreadLocal;
import org.kkonoplev.bali.suiteexec.annotation.UseResources;
import org.kkonoplev.bali.suiteexec.resource.EnvResource;


import java.util.Properties;

@UseResources(list={Browser.class})
public class BaseTestMobile extends BaseTestBali {

	protected static final Logger log = LogManager.getLogger(BaseTestMobile.class);

	public void setUp() throws Exception {
		super.setUp();

		Properties props = TestExecContextThreadLocal.getTestExecContext().getSuiteExecContext().getProperties();
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
			localBaliRunContext.setBrowserType("android");
			localBaliRunContext.getLocalResources().add(new Browser("192.168.95.101:5555","http://127.0.0.1:4723/wd/hub", "android"));
			localBaliRunContext.getLocalResources().add(new EnvResource("Desktop", EnvResource.ALLENV));

			Bali.activateLocalBaliRunContext(localBaliRunContext);
		}
		
		//init threads logger file after test exec conext is initialized and its className is defined
		TestExecContextThreadLocal.getTestExecContext().setCurrentThreadLogAppendersFile();

		
			
	}


	@Override
	public void tearDown(){
		Driver.quite();
		super.tearDown();;
	}
	
}