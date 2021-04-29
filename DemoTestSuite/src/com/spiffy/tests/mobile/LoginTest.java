package com.spiffy.tests.mobile;

/*
 * Author Kirill Konoplev
 * 
 * Login to App
 */

import com.spiffy.framework.utils.Driver;


import com.spiffy.framework.utils.Results;
import org.openqa.selenium.By;

public class LoginTest extends BaseTestMobile {


	public void testDo() throws Exception {


		By userId = By.id("mobileNo");
		By password = By.id("et_password");
		By login_Button = By.id("btn_mlogin");
		By existingUser_login = By.id("btn_mlogin");

		Driver.waitForVisibilityOf(existingUser_login);
		Driver.click(existingUser_login, "log using existing");
		Driver.findWebElement(userId).clear();
		Driver.type(userId, "someone@testvagrant.com" );
		Driver.type(password, "testvagrant123" );
		Driver.click(login_Button, "login");


		if (!Driver.getText(By.id("pageLevelError")).equalsIgnoreCase("Account doesn't exist"))
			Results.addUIError("Expected Account doesn't exist");


	}
	

}
	