package org.kkonoplev.bali.runner.cucumber;


import gherkin.formatter.model.Result;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.suiteexec.SuiteExecContext;
import org.kkonoplev.bali.suiteexec.SuiteExecProcessor;
import org.kkonoplev.bali.suiteexec.SuiteMdl;
import org.kkonoplev.bali.suiteexec.TestExecContext;
import org.kkonoplev.bali.suiteexec.TestExecContextThreadLocal;
import org.kkonoplev.bali.suiteexec.resource.TestExecResource;

/**
 * Created by konokir on 11/6/2014.
 */
public class BaliTestContext {

    protected static final Logger log = LogManager.getLogger(BaliTestContext.class);



    public static void init() throws Exception{
        // this init when started test from Eclipse to create TestExecContext

        log.info("Check text exec context in thread: "+Thread.currentThread().getName());

        // if test is not started from Bali - then suiteExecContext is not initialized.
        // lets do it here
        TestExecContext testExecContext = getTestExecContext();
        if (testExecContext == null){

            log.info("Test Exec context in thread "+Thread.currentThread().getName()+" is null. Will create new!");

            System.setProperty("webdriver.chrome.driver", "C:\\temp\\selenium\\chromedriver.exe");
            System.setProperty("webdriver.firefox.profile", "default");


            SuiteExecProcessor proc = new SuiteExecProcessor();
            SuiteExecContext suiteExecContext = new SuiteExecContext(proc);


            //init result dir
            File resDir = (System.getProperty("test.report.dir") != null) ? new File(System.getProperty("test.report.dir")) : new File("target/junit/reports/");
            resDir.mkdirs();

            suiteExecContext.setResultDirFile(resDir);
            suiteExecContext.setResultDir(resDir.getName());

            //init browser
            SuiteMdl suiteMdl = new SuiteMdl();
            suiteMdl.setCapturemode(false);


            String browser = System.getProperty("browser");
            if (browser == null)
                browser = "FIREFOX";

            suiteMdl.setBrowser(browser);
            suiteExecContext.setSuiteMdl(suiteMdl);

            //todo: init classify report

            Properties props = new Properties();
            props.load(new FileReader("ICS_QA2.properties"));
            suiteExecContext.setProperties(props);


            TestExecContext testExecContextVar = null; //new TestExecContext(suiteExecContext, null);
            testExecContextVar.setCaptureMode(false);

            //add required resources
            ArrayList<TestExecResource> resources= new ArrayList<TestExecResource>();
            testExecContextVar.setResources(resources);

            for (TestExecResource res: testExecContextVar.getResources()){
                res.init(testExecContextVar);
            }

            testExecContextVar.setSuiteExecContext(suiteExecContext);

            TestExecContextThreadLocal.setTestExecContext(testExecContextVar);
            log.info("Test Exec context in thread "+Thread.currentThread().getName()+" is created");

        }


    }

    public static void addPerfomanceResult(String operationName, Date date, long delay) {

        TestExecContext testContext = getTestExecContext();
        if (testContext != null)
            testContext.addPerfomanceResult(operationName, date, delay);

    }

    public static void addError(Result result) {

        TestExecContext testContext = getTestExecContext();
        String errorMsg = result.getErrorMessage();
        
        if (testContext != null){
        	String[] msgSplitted = modifyAndSplitErrorText(errorMsg);
        	//for (String msg: msgSplitted)
        	// todo rework
        	//testContext.addError(result.getError(), msg, null);
        }

    }


    public static String[] modifyAndSplitErrorText(String msg){
    	
    	String msgNew = msg;
    	String msgsSplitted[] = new String[]{msg};
    	
    	int atIndx = msg.indexOf(". Expression:");
    	if (atIndx != -1){
    		
    		int lastIndx = msg.indexOf("at ✽");
    		if (lastIndx != -1){
    			
    			int grIndx  = msg.substring(0, lastIndx).lastIndexOf("(");
    			if (grIndx == -1)
    				grIndx = lastIndx;
    			
    			msgsSplitted = splitMessage(msg.substring(0, atIndx), msg.substring(grIndx));
    		} else {
    			msgsSplitted = splitMessage(msg.substring(0, atIndx), msg.substring(atIndx));
    		}
    		
    	} else {
    		atIndx = msg.indexOf("at ✽");
    		if (atIndx != -1){
    			atIndx -= 3;
    			msgsSplitted = splitMessage(msg.substring(0, atIndx), msg.substring(atIndx));
    		}
    	}
    			
    	return msgsSplitted;
    }
    
    private static String[] splitMessage(String mainMsg, String addMsg) {
		
    	String[] mainMsgs = mainMsg.split("<>");
    	for (int i = 0; i < mainMsgs.length; i++){
    		mainMsgs[i] = mainMsgs[i] + " ,, "+ addMsg;
    		mainMsgs[i] = mainMsgs[i].replaceAll("\\r", "").replaceAll("\\n", "");
    		
    	}
    	
		return mainMsgs;
	}

	public static void addError(Exception e) {

        TestExecContext testContext = getTestExecContext();
        //if (testContext != null)
           // todo:
           // testContext.addError(e, null);
    }

    public void setUp() throws Exception {

        checkTestExecContextInit();
        log.info("Begin of testcase [].");

    }

    public static void checkTestExecContextInit() throws Exception {

        //init TestExecContext and other environment if test is run as single from Eclipse or other IDE for example
        init();
        //TestExecContextThreadLocal.getTestExecContext().setTestCase(clazz);

        //init logger after test is set to context
        TestExecContextThreadLocal.getTestExecContext().setCurrentThreadLogAppendersFile();

    }

    public static void initLogger() throws Exception {

        TestExecContext testContext = getTestExecContext();
        if (testContext != null)
            getTestExecContext().setCurrentThreadLogAppendersFile();

    }

    public void tearDown(){
        log.info("End of testcase " + "[].");
    }

    public static TestExecContext getTestExecContext(){
        return TestExecContextThreadLocal.getTestExecContext();
    }

}
