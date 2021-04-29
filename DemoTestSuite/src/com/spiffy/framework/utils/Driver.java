package com.spiffy.framework.utils;

import java.util.Date;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.selenium.browser.Browser;
import org.kkonoplev.bali.suiteexec.SoftStopException;
import org.kkonoplev.bali.suiteexec.TestExecContextThreadLocal;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.spiffy.tests.BaseTestBali;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.kerala.client.QALog;

public class Driver {
	
	protected static final Logger log = LogManager.getLogger(Driver.class);


	public static void quite() {
		log.info("Exit from driver");
		getWebDriver().quit();
		getBrowser().reset();
	}

	public static void waitForVisibilityOf(By by) {
		WebDriverWait wait = new WebDriverWait(getWebDriver(), 30);
		log.info("wait for "+by);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		log.info("wait for "+by+" done");
	}

	public static void click(String css, String about, boolean log) throws Exception {
		click(css, about);
		if (log)
			BaseTestBali.addNewTestStepBrowser("Click "+about);
	}

	public static void click(String css, String about) throws Exception{
		click(By.cssSelector(css), about);
	}


	public static void dbclick(String css, String about) throws Exception{
		dbclick(By.cssSelector(css), about);
	}
	
	public static void clickRetry(By by, String about, int cnt) throws Exception{
		
		int tries = 0;
		boolean ok = false;
		do {
			
			try {
				click(by, about);
				ok = true;
			} catch (Exception e){
				log.warn("Try "+tries+" -> "+e.toString());
				tries++;
				Thread.sleep(2000);
			}
		
		} while (tries < cnt && !ok);	
		
	}


	public static Browser getBrowser(){
		Browser browser = (Browser) TestExecContextThreadLocal.getTestExecContext().getResource(new RequiredTestResource(Browser.class, ""));
		return browser;
	}

	public static WebDriver getWebDriver(){
		return getBrowser().getWebDriver();
	}

	public static WebDriver getAndroidDriver(){
		return getBrowser().getAndroidDriver();
	}


	public static WebElement findWebElement(String css) throws Exception{
		return findWebElement(By.cssSelector(css));
	}
	
	public static WebElement findWebElement(By by) throws Exception{
		
		checkSoftStop();
		log.info("find web element "+by);
		WebElement elem = null;
		try {
			elem = getWebDriver().findElement(by);
			log.info("done.");
		} catch(NoSuchElementException e){
			log.warn("Cant find element on page: "+by);
		} catch(Exception e){
			log.error("fundWebElement", e);
		}
		return elem;
				
	}
		
	private static void checkSoftStop() throws SoftStopException {
		
		if (BaseTestBali.getTestExecContext().getNeedStop())
			throw new SoftStopException();
		
	}

	public static boolean isElementPresent(String css) throws Exception {
		
		WebElement we = findWebElement(css);
		if (we == null)
			return false;
		else
			return true;
	}
	

	public static boolean isElementDisplayed(String css) throws Exception {
		return isElementDisplayed(By.cssSelector(css));
	}
	
	public static boolean isElementDisplayed(By by) throws Exception {
		
		WebElement we = findWebElement(by);
		if (we == null || !we.isDisplayed())
			return false;
		else
			return true;
	}

	public static void dbclick(By by, String about) throws Exception{
		
		log.info("CLICK "+about+" => "+by);
		WebElement elem = findWebElement(by);
		if (elem == null)
			throw new RuntimeException("Cant find WebElement with "+by);
		
		Actions action = new Actions(Driver.getWebDriver());
		action.moveToElement(elem).doubleClick().build().perform();

		Results.addUICapture("CLICK "+about+" on "+by);
		
	}


	public static void click(By by, String about) throws Exception{
		
		log.info("CLICK "+about+" => "+by);
		WebElement elem = findWebElement(by);
		if (elem == null)
			throw new RuntimeException("Cant find WebElement with "+by);
				
		elem.click();

		Results.addUICapture("CLICK "+about+" on "+by);
		
	}
	

	public static String getText(String css) throws Exception{
		return getText(By.cssSelector(css));
	}
	
	public static String getText(By by) throws Exception{
		log.info("getText "+by);
		WebElement we = findWebElement(by);
		if (we == null)
			throw new RuntimeException("Not able to find item on page: "+by);
		
		String text = we.getText();
		log.info("Text in "+by+" is "+text);
		return text;
	}
	
	public static String getText(By by, String about) throws Exception{
		log.info("getText "+by);
		WebElement we = findWebElement(by);
		if (we == null)
			throw new RuntimeException("Not able to find item on page: "+about);
		
		String text = we.getText();
		log.info("Text in "+by+" is "+text);
		return text;
	}

	
	public static void assignID(By by, String id) throws Exception
	{
	
		log.info("assign ID ="+id+" to "+by);
	    String scriptString = "sId=function(e, i){e.id = i;}; sId(arguments[0], arguments[1]);";
	    ((JavascriptExecutor)getWebDriver()).executeScript(scriptString, findWebElement(by), id);
	     
	}
	
	public static void type(String css, String text) throws Exception{
		
		log.info("type "+text+" to "+css);
		
		WebElement elem = findWebElement(By.cssSelector(css));
		if (elem == null)
			throw new RuntimeException("Cant find element "+css+" for input");
		
		elem.clear();
		elem.sendKeys(text);

		Results.addUICapture("type "+text+" to "+css);

	}

	public static void type(By by, String text) throws Exception{

		log.info("type "+text+" to "+by);

		WebElement elem = findWebElement(by);
		if (elem == null)
			throw new RuntimeException("Cant find element "+by+" for input");

		elem.clear();
		elem.sendKeys(text);

		Results.addUICapture("type "+text+" to "+by);

	}


	public static void maximizeWindow(){
		
		log.info("maximize window");
		getWebDriver().manage().window().maximize();	
		
	}
	
	public static void openUrl(String url) throws InterruptedException{
		openUrl(url, "");
	}
	
	public static void openUrl(String url, String perfComment) throws InterruptedException{
	
		
		log.info("opening url: "+url);
		long beforeMs = System.currentTimeMillis();
		getWebDriver().navigate().to(url);
		long afterMs = System.currentTimeMillis();
		long diff = (afterMs-beforeMs);
		log.info("Loading time:"+diff+" ms");

		Results.addUICapture("open url:"+url);
		
		String urlCut = url;
		
		int l = url.lastIndexOf("/");
		if (l != -1)
			urlCut = url.substring(0, l+1);
		
		String cmnt = "open "+urlCut;
		
		if (perfComment.equals(""))
			cmnt = "open "+perfComment;
			
		TestExecContextThreadLocal.getTestExecContext().addPerfomanceResult(cmnt, new Date(), diff);
		
	}


	public static void moveToElement(String css, String about) throws Exception {
		moveToElement(By.cssSelector(css), about);	
	}

	public static void moveToElement(By by, String about) throws Exception {
		
		log.info("Move mouse to "+about+" on "+by);
		
		Actions action = new Actions(getWebDriver());
		action.moveToElement(findWebElement(by)).build().perform();

		Results.addUICapture("Move mouse to "+about+" on "+by);

	}


	public static String switchToNextWindow() {

		log.info("Switching focus to next window");
		
		String currentWindowId = Driver.getWebDriver().getWindowHandle();
	    Set<String> availableWindowsId = Driver.getWebDriver().getWindowHandles();
	    
	    if (availableWindowsId.size() != 2)
	    	throw new RuntimeException("Expected windows count  = 2, but now have "+availableWindowsId.size());
	    	
	    String winId = "";
	    for (String id : availableWindowsId){
	    	if (!id.equals(currentWindowId)){
	    		Driver.getWebDriver().switchTo().window(id);
	    	}
	    }
	    
	    QALog.addLastTestStepDesc("Switching focus to next window with title " + Driver.getWebDriver().getCurrentUrl());
	    log.info("Current window url: "+ Driver.getWebDriver().getCurrentUrl());
	    log.info("Current window title: "+ Driver.getWebDriver().getTitle());
	    
	    return currentWindowId;

	}


	public static void focus() {
		 	
		 ((JavascriptExecutor) getWebDriver()).executeScript("window.focus();");

	}


	public static void closeCurrentWindow() {
		log.info("Close current focus window");
		BaseTestBali.addNewTestStepBrowser("Close current focus window");
		Driver.getWebDriver().close();
	}


	public static void switchToWindow(String curWinId) {
		log.info("Switching to win id: "+curWinId);
		Driver.getWebDriver().switchTo().window(curWinId);
	}


	public static void switchToFirstWindow() {
		
		Set<String> availableWindowsId = Driver.getWebDriver().getWindowHandles();
	    for (String id1 : availableWindowsId){
	    		Driver.getWebDriver().switchTo().window(id1);
	    		BaseTestBali.addNewTestStepBrowser("Switch to window "+id1);	    		
	    		break;
	    }
	    		
	}


	public static void closeOtherWindows() {
		Set<String> availableWindowsId = Driver.getWebDriver().getWindowHandles();
		int size = availableWindowsId.size();
		
	    for (String id1 : availableWindowsId){
	    		if (size == 1)
	    			break;
	    		Driver.getWebDriver().switchTo().window(id1);
	    		Driver.getWebDriver().close();
	    		size--;
	    		break;
	    }
	}


	


	



}
