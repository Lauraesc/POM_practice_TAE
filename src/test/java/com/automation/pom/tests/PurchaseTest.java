package com.automation.pom.tests;

import com.automation.pom.pages.CartPage;
import com.automation.pom.pages.CheckoutPage;
import com.automation.pom.pages.HomePage;
import com.automation.pom.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PurchaseTest {


    private WebDriver driver;

    private LoginPage loginPage;
    private HomePage homePage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get(LoginPage.LOGIN_URL);


        loginPage = new LoginPage(driver);
        homePage = loginPage.loginAs("standard_user", "secret_sauce");
    }

    @Test
    public void testPurchaseFlow() {

        homePage.addRandomItemToCart();
        homePage.goToCart();


        cartPage = new CartPage(driver);
        cartPage.clickCheckout();

        checkoutPage = new CheckoutPage(driver);
        checkoutPage.completeCheckout("Laura", "Escobar", "110111");


        String successMsg = checkoutPage.getSuccessMessage();
        Assert.assertEquals(successMsg, "Thank you for your order!", "La compra debe completarse correctamente");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}