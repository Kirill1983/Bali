package com.spiffy.framework.utils;

import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.IFrameArtifactsBuilder;
import org.kkonoplev.bali.classifyreport.TestLogArtifactsBuilder;
import org.kkonoplev.bali.classifyreport.WarningCaseArtifactsBuilder;
import org.kkonoplev.bali.selenium.browser.Browser;
import org.kkonoplev.bali.selenium.browser.BrowserArtifactsBuilder;
import org.kkonoplev.bali.selenium.browser.MobileArtifactsBuilder;
import org.kkonoplev.bali.suiteexec.TestExecContext;
import org.kkonoplev.bali.suiteexec.TestExecContextThreadLocal;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;

public class Results {
	
	protected static final Logger log = LogManager.getLogger(Results.class);
	
	public static void addError(String errorText){
		
		TestLogArtifactsBuilder testlogArtifactsBuilder = new TestLogArtifactsBuilder(TestExecContextThreadLocal.getTestExecContext());
		
		getTestExecContext().addError(errorText, 
				new WarningCaseArtifactsBuilder[]{
				testlogArtifactsBuilder});
	}

	public static void addUIError(String errorText){
		
		Browser browser = (Browser) TestExecContextThreadLocal.getTestExecContext().getResource(new RequiredTestResource(Browser.class, ""));
		BrowserArtifactsBuilder browserArtifactsBuilder = new BrowserArtifactsBuilder(browser, TestExecContextThreadLocal.getTestExecContext());
		TestLogArtifactsBuilder testlogArtifactsBuilder = new TestLogArtifactsBuilder(TestExecContextThreadLocal.getTestExecContext());
		
		log.error(errorText);
		getTestExecContext().addError(errorText, 
				new WarningCaseArtifactsBuilder[]{
				browserArtifactsBuilder,
				testlogArtifactsBuilder});
	}

	public static void addContentError(String errorText, String content){
		
		Browser browser = (Browser) TestExecContextThreadLocal.getTestExecContext().getResource(new RequiredTestResource(Browser.class, ""));
		IFrameArtifactsBuilder browserArtifactsBuilder = new IFrameArtifactsBuilder(content, TestExecContextThreadLocal.getTestExecContext());
		TestLogArtifactsBuilder testlogArtifactsBuilder = new TestLogArtifactsBuilder(TestExecContextThreadLocal.getTestExecContext());
		
		log.error(errorText);
		getTestExecContext().addError(errorText, 
				new WarningCaseArtifactsBuilder[]{
				browserArtifactsBuilder,
				testlogArtifactsBuilder});
	}

	public static void addUICapture(String evt){
		
		if (!getTestExecContext().getCaptureMode())
			return;
		
		String event = evt+" "+new Date()+" ("+System.currentTimeMillis()+")";
		Browser browser = (Browser) TestExecContextThreadLocal.getTestExecContext().getResource(new RequiredTestResource(Browser.class, ""));
		getTestExecContext().addCaptureEvent(event,
				new WarningCaseArtifactsBuilder[]{
						new BrowserArtifactsBuilder(browser, TestExecContextThreadLocal.getTestExecContext())});
	}

	
	public static TestExecContext getTestExecContext(){
		return TestExecContextThreadLocal.getTestExecContext();
	}
	
}
