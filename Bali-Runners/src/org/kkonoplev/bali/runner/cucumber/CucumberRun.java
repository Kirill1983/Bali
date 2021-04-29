package org.kkonoplev.bali.runner.cucumber;



import cucumber.runtime.ClassFinder;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by konokir on 10/29/2014.
 */
public class CucumberRun {

    public static void main(String[] argv) throws Throwable {

        String[] arg = new String[]{
            "--format",
            "org.kkonoplev.bali.runner.cucumber.CukeReporter",
            "--name",
            "exceptions, when create custom instruments for IRS_CURVE, IRS_BUTTERFLY",
            "--glue",
            "C:\\abx1\\test\\rel-1\\src\\test\\groovy\\tests",
            "C:/abx1/test/rel-1/src/test/resources/TradeLog/BaliDemoRun.feature"
        };

        byte exitstatus = run(arg, Thread.currentThread().getContextClassLoader());
        System.exit(exitstatus);
    }

    /**
     * Launches the Cucumber-JVM command line.
     *
     * @param argv        runtime options. See details in the {@code cucumber.api.cli.Usage.txt} resource.
     * @param classLoader classloader used to load the runtime
     * @return 0 if execution was successful, 1 if it was not (test failures)
     * @throws java.io.IOException if resources couldn't be loaded during the run.
     */
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
