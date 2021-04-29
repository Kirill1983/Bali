package com.spiffy.framework.utils;

import org.kkonoplev.bali.selenium.browser.Browser;
import org.kkonoplev.bali.suiteexec.TestExecContextThreadLocal;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import ru.kerala.client.IDocScreenShot;

public class DocServiceBrowser implements IDocScreenShot {
	
	public String getScreenShotSource() {
	
		Browser browser = (Browser) TestExecContextThreadLocal.getTestExecContext().getResource(new RequiredTestResource(Browser.class, ""));
		TakesScreenshot tsDriver = (TakesScreenshot) browser.getWebDriver();
	    String shot = tsDriver.getScreenshotAs(OutputType.BASE64);
		return shot;
		
	}

	public void takeScreenShot() {
	}
	
	
	
}
