package com.automation.pom.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class CartPage extends BasePage {

    private final WebDriverWait wait;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(css = ".cart_button")
    private List<WebElement> removeButtons;

    public CartPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        waitUntilLoaded();
    }

    public CartPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/cart.html"));
        wait.until(ExpectedConditions.visibilityOf(checkoutButton));
        return this;
    }

    public int getCartItemCount() {
        waitUntilLoaded();
        return cartItems.size();
    }

    public void clickCheckout() {
        waitUntilLoaded();
        scrollIntoView(checkoutButton);
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlToBe("https://www.saucedemo.com/checkout-step-one.html"),
                ExpectedConditions.visibilityOfElementLocated(By.id("first-name"))
        ));
    }

    public void removeAllItems() {
        waitUntilLoaded();
        for (WebElement btn : removeButtons) {
            try {
                scrollIntoView(btn);
                wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            }
        }
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".cart_item"), 0));
    }

    public boolean isEmpty() {
        waitUntilLoaded();
        try {
            wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".cart_item"), 0));
        } catch (TimeoutException ignored) {}
        return driver.findElements(By.cssSelector(".cart_item")).isEmpty();
    }

    public boolean isBadgeAbsent() {
        return driver.findElements(By.cssSelector("[data-test='shopping-cart-badge']")).isEmpty();
    }
}