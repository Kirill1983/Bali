package org.kkonoplev.bali.selenium.browser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.WarningCaseArtifactsBuilder;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseArtifact;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseHREFArtifact;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseIMGArtifact;
import org.kkonoplev.bali.suiteexec.TestExecContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class BrowserArtifactsBuilder implements WarningCaseArtifactsBuilder {

	
	protected static final Logger log = LogManager.getLogger(BrowserArtifactsBuilder.class);
	
	private Browser browser;
	private File dir;
	private String file = "";
	private String addPath = "";
	private String addPathWeb = "";
	
	public BrowserArtifactsBuilder(Browser browser, TestExecContext testExecContext){
		this.browser = browser;
		
		File reportDir = testExecContext.getSuiteExecContext().getResultDirFile();
		if (testExecContext.getClassName().lastIndexOf(".") != -1){
			addPath = testExecContext.getClassName().substring(0, testExecContext.getClassName().lastIndexOf(".")).replaceAll("\\.", "\\" + File.separator);
			addPathWeb = testExecContext.getClassName().substring(0, testExecContext.getClassName().lastIndexOf(".")).replaceAll("\\.", "/");
		}
		
		dir = new File(reportDir, addPath);

		// if run from Eclipse need to create dir self
		if (!dir.exists())
			dir.mkdirs();

		long time = System.currentTimeMillis();
		file = testExecContext.getRunnableNode().getName()+"-"+testExecContext.getThreadId()+"-"+time+".jpg";
	}

	@Override
	public List<WarningCaseArtifact> build() {
		String url = browser.getWebDriver().getCurrentUrl();
		doScreenshot(browser.getWebDriver(), dir, file);

		List<WarningCaseArtifact> list = new ArrayList<WarningCaseArtifact>();
		list.add(new WarningCaseHREFArtifact("url", url));
		
		String filePath = file;
		if (!addPathWeb.equals(""))
			filePath = addPathWeb+"/"+file;
		
		list.add(new WarningCaseIMGArtifact(filePath));
		return list;
	}
	
	protected static void doScreenshot(WebDriver webDriver, File packageDir,
			String file) {

		if (webDriver == null)
			return;

		try {

			// need package dir
			packageDir.mkdirs();
			File screenShotFile = new File(packageDir, file);
		
			//String shot = selenium.captureScreenshotToString();
			TakesScreenshot tsDriver = (TakesScreenshot) webDriver;
		    String shot = tsDriver.getScreenshotAs(OutputType.BASE64);
			
			OutputStream out = new FileOutputStream(screenShotFile);
			out.write(Base64.decodeBase64(shot));
			out.close();

		} catch (Exception e) {
			log.warn("Can't retrieve screenshot from Selenium!", e);
		}
	}



	
	

}
