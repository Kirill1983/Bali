package org.kkonoplev.bali.runner.testng;

import org.kkonoplev.bali.classifyreport.TestLogArtifactsBuilder;
import org.kkonoplev.bali.classifyreport.WarningCaseArtifactsBuilder;
import org.kkonoplev.bali.suiteexec.TestExecContext;
import org.kkonoplev.bali.suiteexec.TestExecContextThreadLocal;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class BaliTestListener implements ITestListener {

	@Override
	public void onTestStart(ITestResult result) {
		System.out.println("on test method " +  getTestMethodName(result) + " start");
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println("on test method " + getTestMethodName(result) + " success");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println("on test method " + getTestMethodName(result) + " failure");
		addBaliError(result.getThrowable().getMessage());

	}
	
	public static void addBaliError(String errorText){
		
		TestExecContext testExecContext = TestExecContextThreadLocal.getTestExecContext();
		if (testExecContext != null){
			TestLogArtifactsBuilder testlogArtifactsBuilder = new TestLogArtifactsBuilder(TestExecContextThreadLocal.getTestExecContext());
			testExecContext.addError(errorText, new WarningCaseArtifactsBuilder[]{testlogArtifactsBuilder});
		}
		
	}


	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println("test method " + getTestMethodName(result) + " skipped");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("test failed but within success % " + getTestMethodName(result));
	}

	@Override
	public void onStart(ITestContext context) {
		System.out.println("on start of test " + context.getName());
	}

	@Override
	public void onFinish(ITestContext context) {
		System.out.println("on finish of test " + context.getName());
	}
	
	private static String getTestMethodName(ITestResult result) {
		return result.getMethod().getConstructorOrMethod().getName();
	}
}