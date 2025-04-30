package com.base;

import com.Pages.LoginPage;
import com.testUtils.Utilities;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(com.Listeners.CustomEmailableReport.class)

public class TestRun extends Utilities {

    @Test(enabled = true)
    public void TC_01_Login() {
        try{
            LoginPage loginPage = new LoginPage();
            loginPage.login(driver);
        } catch (Exception e) {
        }
    }
}
