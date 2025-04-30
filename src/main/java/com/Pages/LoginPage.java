package com.Pages;

import com.testUtils.Utilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends Utilities {
    public LoginPage() {
        super();
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[@id = '']")
    public WebElement username;

    public void login(WebDriver driver) throws InterruptedException {
        Thread.sleep(1500);
        waitForElement(driver, username);
        username.sendKeys(props.getProperty("username"));

    }

}
