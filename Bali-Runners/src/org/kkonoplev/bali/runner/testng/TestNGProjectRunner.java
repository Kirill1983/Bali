package org.kkonoplev.bali.runner.testng;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.runner.ProjectRunner;
import org.kkonoplev.bali.runner.RunnableItem;
import org.testng.TestNG;

public class TestNGProjectRunner implements ProjectRunner {

	protected static final Logger log = LogManager.getLogger(TestNGProjectRunner.class);
	
	@Override
	public void run(RunnableItem runnableItem) throws Exception {
		
		TestNGRunnableItem item = (TestNGRunnableItem) runnableItem;
		Thread.currentThread().setContextClassLoader(item.getProject().getClassLoader());
	    
		BaliTestListener baliListener = new BaliTestListener();
		TestNG testng = new TestNG();
  	    Class[] classes = new Class[]{item.getTestClass()};
		testng.setTestClasses(classes);
		testng.addListener(baliListener);
		testng.run();

	}

}
