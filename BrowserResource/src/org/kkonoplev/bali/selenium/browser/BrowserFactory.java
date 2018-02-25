package org.kkonoplev.bali.selenium.browser;

import java.util.ArrayList;
import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.suiteexec.resource.ResourcePoolFactory;
import org.kkonoplev.bali.suiteexec.resource.TestExecResource;


public class BrowserFactory  implements ResourcePoolFactory {

	protected static final Logger log = LogManager.getLogger(BrowserFactory.class);
	
	public Class<? extends TestExecResource> getResourceClass(){
		return Browser.class;
	}
	

	public ArrayList<TestExecResource> load(String cmd) {
		
		String[] args = cmd.split(",");
		
		int browserPoolSize = 1;
		String hubURL = "";
		boolean needCheckJS = false;		
		String browserType = Browser.BROWSER_TYPE.CHROME.getType();
		
		try {
			
			if (args != null){
				if (args.length >=1)
					browserPoolSize = Integer.valueOf(args[0]);	
				
				if (args.length >=2)
					hubURL = args[1];
					
				if (args.length >=3)
					needCheckJS = (args[2].equalsIgnoreCase("true")) ? true: false;
				
				if (args.length >=4)
					browserType = args[3];
			}
			
		} catch(Exception e){
				log.warn(e,e);
		}
		
		// random a-z
		Random r = new Random();
		char c = (char)(r.nextInt(26) + 'a');
		String addChar = c+"";
		
		ArrayList<TestExecResource> resources = new ArrayList<TestExecResource>(browserPoolSize);
		for (int i = 1;	i <= browserPoolSize; i++){
			String name = browserType+"_"+i+addChar;
			Browser browser = new Browser(name, hubURL, browserType);
			if (!resources.contains(browser))
				resources.add(browser);
		}
		
		return resources;
	}


	@Override
	public String getLoadCmdHelp() {
		return "cmd=[int number(def=1)], [String hubURL (def=)], [Browser type={ie,firefox,chrome,chromeheadless,phantomjs}, (def=undefined)] adds number of browsers ";
	}

}
