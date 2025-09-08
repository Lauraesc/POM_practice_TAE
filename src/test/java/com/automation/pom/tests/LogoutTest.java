package com.automation.pom.tests;

import com.automation.pom.pages.HomePage;
import com.automation.pom.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LogoutTest extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setUp() {
        driver.manage().deleteAllCookies();
        driver.get(LoginPage.LOGIN_URL);
        loginPage = new LoginPage(driver);
    }

    @Test
    public void logoutShouldRedirectToLoginPage() {
        homePage = loginPage.loginAs("standard_user", "secret_sauce");
        homePage.logout();
        Assert.assertTrue(driver.findElement(By.id("login-button")).isDisplayed(),
                "Login button should be visible after logout");
        System.out.println("Current URL after logout: " + driver.getCurrentUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void takeScreenshotIfFailed(ITestResult result) throws Exception {
        if (!result.isSuccess() && driver != null) {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String fileName = "screenshot_" + result.getName() + "_" + System.currentTimeMillis() + ".png";
            Files.copy(screenshot.toPath(), Paths.get(fileName));
            System.out.println("Screenshot saved as " + fileName);
        }
    }
}