/* 
 * Copyright � 2011 Konoplev Kirill
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
 */

package org.kkonoplev.bali.init;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;


public final class BaliProperties {
	
	public static final String BALI_PROJECT_DIR = "bali.core.dir";
	public final static String BALI_SUITES_DIR = "bali.suites.path";
	public static final String BALI_RESULTS_DIR = "bali.results.dir";
	public static final String BALI_OPTIONS_DIR = "bali.options.dir";
	public static final String BALI_PROJECTS_CONFIG = "bali.projects.config";
	public static final String BALI_SMTP_HOST = "bali.smtp.host";
	

	public static final String WEBDRIVER_FF_PROFILE = "webdriver.firefox.profile";
	public static final String WEBDRIVER_CHROME_DRIVER = "webdriver.chrome.driver";
	public static final String WEBDRIVER_PHANTOMJS_DRIVER = "phantomjs.binary.path";

	
		
	public static final String TEST_EXECUTORS_COUNT = "test.executors.count";
	public static final String INIT_EXECUTORS_COUNT = "init.executors.count";
	
	
	
	private String BaliProjectDir = "c:\\projects\\bali\\";	
	private String BaliResultsDir = "C:\\projects\\Bali\\results";
	private String BaliProjectsConfig = "C:\\projects\\Bali\\conf\\projects.xml";
	private String BaliSuitesDir  = "C:\\projects\\Bali\\conf\\suites";
	private String BaliOptionsDir = "C:\\projects\\Bali\\conf\\options\\";
	private String WebdriverChromeDriver = "C:\\selenium\\chromedriver.exe";
	private String WebdriverPhantomJSDriver = "/home/user/selenium/phantomjs";
	
	private String BaliSmtpHost = "smtphub.uk.mail.db.com";
	
	private int TestExecutorsCount = 3;
	private int InitExecutorsCount = 1;
	   
   
    private static final Logger LOG = Logger.getLogger(BaliProperties.class);
    private static BaliProperties INSTANCE = null;

 

    //disable constructor
    private BaliProperties() {
    }

    public static synchronized boolean init() {
       if (INSTANCE != null) {
            return false;
        }

        LOG.info("loading saas.properties...");
        INSTANCE = new BaliProperties();
        try {
        	String propertiesPath = "C:\\Temp\\projects\\Bali\\conf\\bali.properties";
        	
        	String loadPath = System.getenv("BALI_PROPERTIES_PATH");
        	if (loadPath != null)
        		propertiesPath = loadPath;
        		
            File file = new File(propertiesPath);
            
            Properties props = new Properties();
            props.load(new FileInputStream(file.getCanonicalPath()));

            //update instance properties
            INSTANCE.BaliProjectDir = getValue(props, BALI_PROJECT_DIR, INSTANCE.BaliProjectDir);
            INSTANCE.BaliSuitesDir = getValue(props, BALI_SUITES_DIR, INSTANCE.BaliSuitesDir);
            INSTANCE.BaliResultsDir = getValue(props, BALI_RESULTS_DIR, INSTANCE.BaliResultsDir);
            INSTANCE.BaliProjectsConfig = getValue(props, BALI_PROJECTS_CONFIG, INSTANCE.BaliProjectsConfig);  
            INSTANCE.BaliOptionsDir = getValue(props, BALI_OPTIONS_DIR, INSTANCE.BaliOptionsDir); 
            INSTANCE.BaliSmtpHost = getValue(props, BALI_SMTP_HOST, INSTANCE.BaliSmtpHost); 
            INSTANCE.WebdriverChromeDriver = getValue(props, WEBDRIVER_CHROME_DRIVER, INSTANCE.WebdriverChromeDriver);
            INSTANCE.WebdriverPhantomJSDriver = getValue(props, WEBDRIVER_PHANTOMJS_DRIVER, INSTANCE.WebdriverPhantomJSDriver);

            INSTANCE.TestExecutorsCount = getValue(props, TEST_EXECUTORS_COUNT, INSTANCE.TestExecutorsCount);  
            INSTANCE.InitExecutorsCount = getValue(props, INIT_EXECUTORS_COUNT, INSTANCE.InitExecutorsCount);  

            System.setProperty(WEBDRIVER_FF_PROFILE, "default");
		    System.setProperty(WEBDRIVER_CHROME_DRIVER, INSTANCE.WebdriverChromeDriver);
			System.setProperty(WEBDRIVER_PHANTOMJS_DRIVER, INSTANCE.WebdriverPhantomJSDriver);

		    
          
            LOG.info("bali.properties loaded successfully");
        } catch (Exception ex) {
            LOG.warn("error loading saas.properties");
            LOG.warn(ex, ex);
        }

        return true;
    }
    public static void dinit() {
        INSTANCE = null;
        LOG.info("destroyed bali.properties successfully");
    }

    public static String print() {
        StringBuilder b = new StringBuilder("\n#-------------------------bali.properties-------------------------#\n");
        b.append("  BaliProjectDir                 = ").append(INSTANCE.BaliProjectDir).append("\n");
        b.append("  BaliSuitesDir                  = ").append(INSTANCE.BaliSuitesDir).append("\n");
        b.append("  BaliResultsDir                 = ").append(INSTANCE.BaliResultsDir).append("\n"); 
        b.append("  BaliOptionsDir                 = ").append(INSTANCE.BaliOptionsDir).append("\n"); 
        b.append("  BaliProjectsConfig             = ").append(INSTANCE.BaliProjectsConfig).append("\n");  
        b.append("  webdriver.firefox.profile      = ").append(System.getProperty(WEBDRIVER_FF_PROFILE)).append("\n");  
        b.append("  Path to chrome driver          = ").append(System.getProperty(WEBDRIVER_CHROME_DRIVER)).append("\n");
        b.append("  Path to phantomJS driver       = ").append(System.getProperty(WEBDRIVER_PHANTOMJS_DRIVER)).append("\n");  

        b.append("  Test Executors count           = ").append(INSTANCE.TestExecutorsCount).append("\n");
        b.append("  Init Executors count           = ").append(INSTANCE.InitExecutorsCount).append("\n");  

        return b.toString();
    }


    //static getters for all the properties

    public static String getBaliProjectDir()         	{return INSTANCE.BaliProjectDir;}
    public static String getBaliSuitesDir()         	{return INSTANCE.BaliSuitesDir;}
    public static String getBaliResultsDir()         	{return INSTANCE.BaliResultsDir;}  
    public static String getBaliProjectsConfig()   	    {return INSTANCE.BaliProjectsConfig;} 
    public static String getBaliOptionsDir()        	{return INSTANCE.BaliOptionsDir;} 
    public static int getTestExecutorCount()    		{return INSTANCE.TestExecutorsCount;}
    public static int getInitExecutorCount()    		{return INSTANCE.InitExecutorsCount;}
    public static String getSmtpMailHost()    			{return INSTANCE.BaliSmtpHost;}


    private static String getValue(Properties props, String name, String defaultValue) {
        String value = props.getProperty(name);
        return (value != null && value.trim().length() > 0) ? value : defaultValue;
    }
    
    private static int getValue(Properties props, String name, int defaultValue) {
        String value = props.getProperty(name);
        return (value != null && value.trim().length() > 0) ? Integer.parseInt(value) : defaultValue;
    }
    
    public static Map<String, String> getOptionsList(String rootDir){
    	
    	File dir = new File(rootDir);
		LOG.info("DIR="+dir.getAbsolutePath());

		File[] files = dir.listFiles();
		Map<String, String> list = new LinkedHashMap<String, String>();
	
		
		for (File f: files){
			String r = 	f.getName();
			r = r.replace(".properties", "");
			if (r.equals(""))
				continue;	
			
			list.put(r, r);			
		}
		
		return list;	
	}
    
	public static Map<String, String> getBrowsersList(){
    	
		Map<String, String> list = new LinkedHashMap<String, String>(5);
		
		list.put("chrome", "Chrome");
		list.put("chromeheadless", "ChromePhantom");
		list.put("chrome8090p", "Chrome8090p");
		list.put("firefox", "Firefox");
		list.put("ie7", "IE7");
		list.put("ie8", "IE8");
		list.put("ie9", "IE9");
		list.put("phantom", "PhantomJS");		

		return list;	
	}

	
    
    
 
}