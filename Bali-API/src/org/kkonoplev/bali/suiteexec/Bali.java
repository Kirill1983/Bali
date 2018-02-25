package org.kkonoplev.bali.suiteexec;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.suiteexec.SuiteExecContext;
import org.kkonoplev.bali.suiteexec.SuiteExecContextLocalBuilder;
import org.kkonoplev.bali.suiteexec.SuiteMdl;
import org.kkonoplev.bali.suiteexec.TestExecContext;
import org.kkonoplev.bali.suiteexec.TestExecContextThreadLocal;
import org.kkonoplev.bali.suiteexec.resource.TestExecResource;

public class Bali {
	
	
	protected static final Logger log = LogManager.getLogger(Bali.class);
	
	public static void activateLocalBaliRunContext(LocalRunBaliContext localBaliRunContext) throws Exception{
		
		log.info("Test Exec context in thread "+Thread.currentThread().getName()+" is null. Will create new!");
		
		SuiteMdl suiteMdl = new SuiteMdl();	
		
		suiteMdl.setOptions(localBaliRunContext.getOptions());
		// only 1 test in 1 thread, default project name = local
		suiteMdl.setTests("local#"+localBaliRunContext.getLocalClass()+"#1");
		
		suiteMdl.setCapturemode(localBaliRunContext.isCaptureMode());
		suiteMdl.setBrowser(localBaliRunContext.getBrowserType());	
		
		// set where to source for options
		SuiteExecContextLocalBuilder builder = new SuiteExecContextLocalBuilder();
		SuiteExecContext suiteContext = builder.build(suiteMdl, localBaliRunContext.getResultDir(), localBaliRunContext.getPropertiesDir());
		
		TestExecContext testExecContext = suiteContext.getTestExecContexts().get(0);
		//add required resources to context
		testExecContext.setResources(localBaliRunContext.getLocalResources());

		log.info("Initialize local resources");
		for (TestExecResource res: testExecContext.getResources())
			res.init(testExecContext);
		
		TestExecContextThreadLocal.setTestExecContext(testExecContext);
		log.info("Test Exec context in thread "+Thread.currentThread().getName()+" is instantiated");

	}

	

}
