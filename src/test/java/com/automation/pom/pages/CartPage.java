package com.automation.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(css = ".cart_button")
    private List<WebElement> removeButtons;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }


    public CartPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("cart.html"));

        try {
            wait.until(ExpectedConditions.visibilityOf(checkoutButton));
        } catch (Exception e) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkout")));
        }
        return this;
    }


    public int getCartItemCount() {
        waitUntilLoaded();
        return cartItems.size();
    }


    public void clickCheckout() {
        waitUntilLoaded();

        By checkoutBy = By.id("checkout");


        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(checkoutBy));
                wait.until(ExpectedConditions.elementToBeClickable(btn));
                scrollIntoView(btn);


                try {
                    btn.click();
                    if (waitForStepOneQuick()) return;
                } catch (Exception ignored) {}


                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                    if (waitForStepOneQuick()) return;
                } catch (Exception ignored) {}


                try {
                    new Actions(driver).moveToElement(btn).click().perform();
                    if (waitForStepOneQuick()) return;
                } catch (Exception ignored) {}


                try {
                    btn.sendKeys(Keys.ENTER);
                    if (waitForStepOneQuick()) return;
                } catch (Exception ignored) {}


                sleep(250);

            } catch (StaleElementReferenceException sere) {

                sleep(200);
            }
        }


        driver.navigate().to("https://www.saucedemo.com/checkout-step-one.html");


        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("checkout-step-one.html"),
                ExpectedConditions.presenceOfElementLocated(By.id("first-name"))
        ));
    }


    public void removeAllItems() {
        waitUntilLoaded();
        for (WebElement btn : removeButtons) {
            safeClick(btn);
        }
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".cart_item"), 0));
    }


    private boolean waitForStepOneQuick() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4)).until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("checkout-step-one.html"),
                    ExpectedConditions.presenceOfElementLocated(By.id("first-name"))
            ));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void safeClick(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            scrollIntoView(element);
            element.click();
        } catch (Exception e) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            } catch (Exception jsEx) {
                throw e;
            }
        }
    }

    private void scrollIntoView(WebElement el) {
        try {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (Exception ignored) {}
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}