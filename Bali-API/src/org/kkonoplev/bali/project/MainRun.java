/* 

 * Copyright � 2011 Kirill Konoplev
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
 * 
 */

package org.kkonoplev.bali.project;

import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.kkonoplev.bali.services.ProjectService;
import org.kkonoplev.bali.suiteexec.SuiteExecContext;
import org.kkonoplev.bali.suiteexec.SuiteExecContextBuilder;
import org.kkonoplev.bali.suiteexec.SuiteExecProcessor;
import org.kkonoplev.bali.suiteexec.SuiteMdl;

public class MainRun {

	/**
	 * @param args
	 * @throws Exception 
	 */
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		initLogger();
		ProjectService projectSvc = new ProjectService("C:\\projects\\Bali\\conf\\projects.xml");
		int a = 5;
		
		SuiteExecProcessor suiteExecProcessor = new SuiteExecProcessor(projectSvc, 1);
		
		
		SuiteMdl suiteMdl = new SuiteMdl();
		suiteMdl.setOptions("ENV_A");
		
		suiteMdl.setTests("Express5_BE#carrier.CheckStateTest#1");
		String propsdir = "C:\\projects\\Bali\\conf\\options\\";
		String resdir = "C:\\projects\\Bali\\conf\\results\\";
		

		//Project project = projectSvc.getProject("cucumber");
		//TreeNode treeNode = project.getTreeNode("DEMO.DemoRun.outline1.scenarious1");
		
		//SuiteExecContext suiteContext = (new SuiteExecContextBuilder()).build(suiteMdl, suiteExecProcessor, resdir, propsdir, new HashMap<String, String>(0)); 
		//suiteExecProcessor.addSuiteToJobQueque(suiteContext);
		/*
		ClassLoader loader = projectSvc.getProject("cucumber").getClassLoader();
		URLClassLoader urlloader = (URLClassLoader) (loader);
 		
		
		URL[] urls = ((URLClassLoader) (loader)).getURLs();
		
		int i = 1;
		for (URL url: urls){
			System.out.println((i++)+"."+url);
			
		}*/
		
		//Class.forName( "oracle.jdbc.driver.OracleDriver");
		//Class.forName( "oracle.jdbc.OracleDriver");
		
		//loader.loadClass("groovy.sql.Sql");
		
		//Groovy.sql.Sql sql;
		
		
		//suiteExecProcessor.notifyExecutors();
		
		
		/*
		URLClassLoader loader = projectSvc.getProject("ent").getClassLoader();
		for (URL url :loader.getURLs()){
			System.out.println(url.toString());
		}
		
		//Class sfdcmeta = Class.forName("org.kkonoplev.cloud.framework.saasapp.connections.SFDCmetadata", true, loader);J
		//Object obj = sfdcmeta.getConstructor().newInstance();
		Thread.currentThread().setContextClassLoader(loader);
		
		Class sfdcmeta = loader.loadClass("org.salesforce.samples.Samples");
		//Class sfdcmeta = Class.forName("org.salesforce.samples.Samples", true, loader);
		Object obj = sfdcmeta.getConstructor().newInstance();
		*/
	
		
	    

	}

	private static void initLogger() {

		Properties p = new Properties();

		p.setProperty("log4j.logger.demo", "trace, INFA, console");
	    p.setProperty("log4j.logger.org.kkonoplev", "trace, BALI, console");
	    p.setProperty("log4j.logger.com.db.abx.test", "trace, BALI, console");
	    //p.setProperty("log4j.additivity.org.kkonoplev.cloud", "false");   
		p.setProperty("log4j.appender.BALI", "org.kkonoplev.bali.common.logger.ThreadsAppender"); 
	    p.setProperty("log4j.appender.BALI.layout", "org.kkonoplev.bali.common.logger.LogHTMLLayout");  

		// for system out println
		p.setProperty("log4j.appender.console",	"org.apache.log4j.ConsoleAppender");
		p.setProperty("log4j.appender.console.encoding", "UTF-8");
        p.setProperty("log4j.appender.console.Threshold", "INFO");
		p.setProperty("log4j.appender.console.layout",	"org.apache.log4j.PatternLayout");
		p.setProperty("log4j.appender.console.layout.ConversionPattern","%p %m%n");

		PropertyConfigurator.configure(p);
		
	}

}
