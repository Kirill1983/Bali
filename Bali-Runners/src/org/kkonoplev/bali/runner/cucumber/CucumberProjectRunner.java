package org.kkonoplev.bali.runner.cucumber;


import java.io.IOException;
import java.util.ArrayList;

import org.kkonoplev.bali.runner.ProjectRunner;
import org.kkonoplev.bali.runner.RunnableItem;
import org.kkonoplev.bali.suiteexec.TestExecContextThreadLocal;

import cucumber.runtime.ClassFinder;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;

public class CucumberProjectRunner implements ProjectRunner {

	@Override
	public void run(RunnableItem runnableItem) throws Exception {
		CucumberRunnableItem item = (CucumberRunnableItem) runnableItem;

		
		String[] arg = new String[]{
	            "--format",
	            "org.kkonoplev.bali.runner.cucumber.CukeReporter",
	            "--name",
	            item.getScenarioName(),
	            "--glue",
	            item.getStepsDir(),
	            item.getFeatureFile()
	        };

		String envThreadName = ModifyThreadName(Thread.currentThread().getName(), TestExecContextThreadLocal.getTestExecContext().getSuiteExecContext().getSuiteMdl().getOptions().toLowerCase());
		Thread.currentThread().setName(envThreadName);
		Thread.currentThread().setContextClassLoader(item.getCucumberProject().getClassLoader());
		
		byte exitstatus = run(arg, Thread.currentThread().getContextClassLoader());

	}
	
	public final static String envPrefix = "_env_"; 
	
    private String ModifyThreadName(String name, String envName) {
    	String newName = name;
    	int indx = newName.indexOf(envPrefix);
    	if (indx != -1)
    		newName = newName.substring(0, indx)+envPrefix+envName;
    	else 
    		newName = newName+envPrefix+envName;
		return newName;
	}

	public static byte run(String[] argv, ClassLoader classLoader) throws IOException {
    	
        RuntimeOptions runtimeOptions = new RuntimeOptions(asList(argv));

        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        cucumber.runtime.Runtime runtime = new cucumber.runtime.Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
        runtime.run();
        return runtime.exitStatus();
    }

	private static ArrayList<String> asList(String[] argv) {
		ArrayList<String> list = new ArrayList<String>();
		
		for (String s: argv)
			list.add(s);
		
		return list;
	}



}
