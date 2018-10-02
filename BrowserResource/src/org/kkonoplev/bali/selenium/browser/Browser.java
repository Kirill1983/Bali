	package org.kkonoplev.bali.selenium.browser;

import java.net.URL;
import java.util.Set;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.common.utils.FrequencyController;
import org.kkonoplev.bali.suiteexec.TestExecContext;
import org.kkonoplev.bali.suiteexec.resource.TestExecResource;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;


public class Browser extends TestExecResource {
	
	private static final Logger log = Logger.getLogger(Browser.class);
	
	public enum BROWSER_TYPE {
		FF("firefox"),
		IE("ie"),
		CHROME("chrome"),
		CHROME_PHANTOM("chromeheadless"),
		CHROME8090P("chrome8090p"),
		PHANTOM("phantom");
		
		private String type = "chrome";
		
		BROWSER_TYPE(String type){
			this.type = type;
		}
		
		public String getType(){
			return type;
		}
	}
	
	public static final String BROWSER = "browser";
	
	DesiredCapabilities capability;
	WebDriver webdriver = null;
	
	//default browser - FF
	String browserType = BROWSER_TYPE.CHROME.name();
	String lastInitType = "none";
	
	protected static FrequencyController accessFreqController = new FrequencyController(150, 1000, "browser access");
	protected static FrequencyController initFreqController = new FrequencyController(1, 15000, "browser init");

	protected String gridHubURL = "";
	protected boolean needCheckJS = false;

	
	public Browser(){
		type = TestExecResource.BROWSER;
	}
	
	public Browser(String hubURL){
		type = TestExecResource.BROWSER;
		gridHubURL = hubURL;
	}
		

	public Browser(String name, String hubURL){
		this.name = name;
		type = TestExecResource.BROWSER;
		gridHubURL = hubURL;
	}
	

	public Browser(String name, String hubURL, String browser){
		this.name = name;
		type = TestExecResource.BROWSER;
		gridHubURL = hubURL;
		this.browserType =  browser;
	}
	
	
	/*
	 * 
	 * all browsers are different
	 */
	public boolean equals(Object object){
	
		//all browsers are different initialized from null
		return false;		
	}
	
	public boolean useGrid(){
		if (gridHubURL.equals(""))	
			return false;
		else
			return true;
	}
	public WebDriver getWebDriver(){
		return webdriver;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public boolean init(TestExecContext testExecContext) throws Exception {
		
		status = "checking...";
		String browserType = "";
		
		if (testExecContext != null)
			browserType = testExecContext.getSuiteExecContext().getSuiteMdl().getBrowser();
		else
			browserType = this.browserType;
		
			log.info("Started browser "+getHeadText()+" resource init with type="+browserType+", last init "+lastInitType);
			
			if (webdriver == null){
				initFromNull(browserType);
			} else {
				if (!lastInitType.equals(browserType)){
					log.info("Last Init type:"+lastInitType+" is different from requested:"+browserType);
					log.info("quit current browser");
					webdriver.quit();
					initFromNull(browserType);
				} else {
					controlAlive(browserType);
				}
			}	
			
			status = "online";
			log.info(this+" inited succesfully!");
			this.browserType = browserType;
			cleanCookie();
			return true;
			
	}
	
	/*
	 * assert if driver is alive, give 3 times to init it 
	 */
	private void controlAlive(String browserType) throws Exception {
		int tries = 0;
		int maxTries = 3;
		while (!isAlive() && (tries < maxTries)){
			log.info(this+" try init case:"+tries);
			try {
				log.info("browser quite, before re-new");
				webdriver.quit();
				initFromNull(browserType);
			} catch (Exception e){
				log.warn(this+" Exception during init, try case:"+tries);
				log.warn(e,e);
			}
		}				
	}

	private boolean isAlive() {
		try {
			webdriver.switchTo().window(webdriver.getWindowHandle());
		} catch (Exception e){

			log.warn(e,e);
			log.warn(this+" is not alive. Will restart.");
			
			try {
				// quite if possible
				webdriver.quit();				
			} catch (Exception q){				
			}
			
			webdriver = null;			
			return false;
		}
		
		return true;
	}

	private void initFromNull(String browserType) throws Exception {
		
		alignInitSpeed();
		status = browserType+" init...";
		
		if (!useGrid()){		
			if (browserType.equals(BROWSER_TYPE.IE.getType())){				
				webdriver = new InternetExplorerDriver();
			}else if (browserType.equals(BROWSER_TYPE.CHROME.getType())){
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("forceDevToolsScreenshot", true);
				webdriver = new ChromeDriver(options);	
			}else if (browserType.equals(BROWSER_TYPE.CHROME8090P.getType())){
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("forceDevToolsScreenshot", true);
			webdriver = new ChromeDriver(options);	
			}

			else if (browserType.equals(BROWSER_TYPE.CHROME_PHANTOM.getType())){
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("forceDevToolsScreenshot", true);
				options.addArguments("headless");
				options.addArguments("window-size=1920x1080");
				webdriver = new ChromeDriver(options);				
			}
			else if (browserType.equals(BROWSER_TYPE.PHANTOM.getType())){
				webdriver = new PhantomJSDriver();	
			}else {
				webdriver = new FirefoxDriver();
			}
			
		} else {
			capability = getCapability(browserType);
			webdriver = new ScreenShotRemoteWebDriver(new URL(gridHubURL), capability);		
		}				
		
		status = "online";
		lastInitType = browserType;
		
	}

	private void alignInitSpeed() {
		if (initFreqController != null){
			initFreqController.waitIfLimit();			
		}
		
	}

	public DesiredCapabilities getCapability(String browserType) {
		
		if (browserType.equals(BROWSER_TYPE.IE.getType()))
			return DesiredCapabilities.internetExplorer();
		else if (browserType.equals(BROWSER_TYPE.CHROME.getType()))
			return DesiredCapabilities.chrome();
		else if (browserType.equals(BROWSER_TYPE.PHANTOM.getType()))
			return DesiredCapabilities.phantomjs();	
		else 
			return DesiredCapabilities.firefox();
			
	}

	private void cleanCookie() {
		
		if (webdriver == null)
			return;
		
		try {
			
			log.info("Delete all cookie for "+this);
			webdriver.manage().deleteAllCookies();
		    
			Set<Cookie> allCookies = webdriver.manage().getCookies();
			for (Cookie loadedCookie : allCookies) {
				log.info(String.format("%s -> %s", loadedCookie.getName(), loadedCookie.getValue()));
			}
			
		} catch (Exception e){
			log.error("Error cleaning cookies for "+this);
			log.error(e, e);			
		}
	}

	public String toString(){
		
		return "Browser"+name+", type:" + browserType;
		
	}


	public String getDescription(){		
		return browserType+", hub="+gridHubURL+", checkJS="+needCheckJS;
	}

	public boolean matchRequirements(String properties){
	
		if (properties.equals(""))
			return true;
		
		if (properties.equals(browserType))
			return true;
		
		return false;
	
	}
	
	public boolean isFree(TestExecContext testExecContext, String needBrowserType) {
		if (this.testExecContext != null)
			return false;
		return matchRequirements(needBrowserType);
	}

	public boolean isFree(String properties) {
		return (testExecContext == null);
	}
	
	/*
	 * is resource free and applicable for context
	 */
	public boolean isFree(TestExecContext testExecContext) {
		
		if (this.testExecContext != null)
			return false;
		
		String needBrowserType = testExecContext.getSuiteExecContext().getSuiteMdl().getBrowser();
		
		if (needBrowserType.equals(browserType))
			return true;
		
		return false;
	}
	
	
	
	public boolean match(String match) {
		
		if (browserType.contains(match))
			return true;
		
		if (gridHubURL.contains(match))
			return true;
		
		if ((needCheckJS+"").equalsIgnoreCase(match))
			return true;
		
		return false;
	}
	
	

}
