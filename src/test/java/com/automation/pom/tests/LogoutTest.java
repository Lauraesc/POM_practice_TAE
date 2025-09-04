package com.automation.pom.tests;

import com.automation.pom.pages.LoginPage;
import com.automation.pom.pages.HomePage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

import static java.sql.DriverManager.println;

public class LogoutTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(LoginPage.LOGIN_URL);

        loginPage = new LoginPage(driver);
    }

    @Test
    public void logoutShouldRedirectToLoginPage() {
        // Login
        homePage = loginPage.loginAs("standard_user", "secret_sauce");

        // Logout
        homePage.logout();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));

        String currentUrl = driver.getCurrentUrl();
        System.out.println("🔎 Current URL after logout: " + currentUrl);

        Assert.assertTrue(driver.findElement(By.id("login-button")).isDisplayed(),
                "Login button should be visible after logout");

    }


    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        // 👇 comenta esto mientras pruebas
        // if (driver != null) {
        //     driver.quit();
        // }
    }

    @AfterMethod(alwaysRun = true)
    public void takeScreenshotIfFailed(ITestResult result) throws Exception {
        if (!result.isSuccess() && driver != null) {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Nombre único con timestamp y nombre del test
            String fileName = "screenshot_" + result.getName() + "_" + System.currentTimeMillis() + ".png";

            Files.copy(screenshot.toPath(), Paths.get(fileName));
            System.out.println("📸 Screenshot saved as " + fileName);
        }

        if (driver != null) {
            driver.quit(); // 👈 aquí cerramos el navegador
        }
    }

}
