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

import java.net.URL;
import java.net.URLClassLoader;

import org.kkonoplev.bali.common.logger.LocalRunLogger;
import org.kkonoplev.bali.services.ProjectService;

public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		LocalRunLogger.initStdoutLogger();
		ProjectService projectSvc = new ProjectService("C:\\projects\\Bali\\conf\\projects.xml");
		int a = 5;
		projectSvc.getProjectTestsList();
		
		
		//dCaps = new DesiredCapabilities();
		//dCaps.setJavascriptEnabled(true);
		//dCaps.setCapability("takesScreenshot", false);

		//driver = new PhantomJSDriver(dCaps);
		//baseUrl = "http://assertselenium.com/";
		//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				
		//URLClassLoader loader = ((BaseJavaProject)projectSvc.getProject("Express5_BE")).getClassLoader();
		//for (URL url :loader.getURLs()){
		//	System.out.println(url.toString());
		//}
		
		//Class sfdcmeta = loader.loadClass("com.transit.express.dictionary.LoadDictionariesTest");
		
		
		/*
		//Class sfdcmeta = Class.forName("org.kkonoplev.cloud.framework.saasapp.connections.SFDCmetadata", true, loader);
		//Object obj = sfdcmeta.getConstructor().newInstance();
		Thread.currentThread().setContextClassLoader(loader);
		
		Class sfdcmeta = loader.loadClass("org.salesforce.samples.Samples");
		//Class sfdcmeta = Class.forName("org.salesforce.samples.Samples", true, loader);
		Object obj = sfdcmeta.getConstructor().newInstance();
		*/
	
		
	    

	}

}
