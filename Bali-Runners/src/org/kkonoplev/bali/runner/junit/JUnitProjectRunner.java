package org.kkonoplev.bali.runner.junit;

import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.kkonoplev.bali.runner.ProjectRunner;
import org.kkonoplev.bali.runner.RunnableItem;

public class JUnitProjectRunner implements ProjectRunner {

	@Override
	public void run(RunnableItem runnableItem) {
		JUnitRunnableItem item = (JUnitRunnableItem) runnableItem;
		Thread.currentThread().setContextClassLoader(item.getProject().getClassLoader());
		TestResult testResult = new TestResult();	
		TestSuite suite = new TestSuite(item.getTestClass());
		suite.run(testResult);
	}

}
