package com.spiffy.framework.app;

import com.spiffy.framework.utils.Driver;
import com.spiffy.tests.BaseTestBali;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SpiffyApp {

    private String url = "";
    protected static final Logger log = LogManager.getLogger(SpiffyApp.class);
    public SpiffyApp(String url){
        this.url = url;
    }

    public void openLoginPage() throws Exception{

        Driver.openUrl("http://"+url+"/login");

        log.info("Check logout");
        if (Driver.isElementDisplayed("div[class='alert-box alert']") &&
                Driver.getText("div[class='alert-box alert']").equals("You are already signed in.")){
            log.warn("Get: You are already signed in. Need to clean cookies/reload.");
            Driver.getWebDriver().manage().deleteAllCookies();
            Driver.openUrl("http://"+url+"/login");
        }

        BaseTestBali.addNewTestStepBrowser("open login page");
    }


}
