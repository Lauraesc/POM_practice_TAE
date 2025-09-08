package com.automation.pom.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class HomePage extends BasePage {

    private final WebDriverWait wait;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");
    private final By addBtns = By.cssSelector("button[data-test^='add-to-cart-']");

    public HomePage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(addBtns));
    }

    public void addRandomItemToCart() {
        List<WebElement> addButtons = wait.until(d -> d.findElements(addBtns));
        if (addButtons == null || addButtons.isEmpty()) {
            throw new IllegalStateException("No 'Add to cart' buttons found on inventory page");
        }
        WebElement addBtn = addButtons.get(new Random().nextInt(addButtons.size()));
        String addAttr = addBtn.getAttribute("data-test");
        String removeSelector = "button[data-test='" + addAttr.replace("add-to-cart", "remove") + "']";
        scrollIntoView(addBtn);
        addBtn.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(removeSelector)));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("[data-test='shopping-cart-badge']"), "1"
        ));
    }

    public void goToCart() {
        WebElement cart = wait.until(ExpectedConditions.elementToBeClickable(cartLink));
        scrollIntoView(cart);
        cart.click();
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/cart.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkout")));
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutLink);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
    }

    public void addItems(int n) {
        List<WebElement> addButtons = wait.until(d -> d.findElements(By.cssSelector("button[data-test^='add-to-cart-']")));
        if (addButtons == null || addButtons.isEmpty()) {
            throw new IllegalStateException("No 'Add to cart' buttons found on inventory page");
        }
        int target = Math.min(n, addButtons.size());
        for (int i = 0; i < target; i++) {
            WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(addButtons.get(i)));
            String addAttr = addBtn.getAttribute("data-test");
            String removeSelector = "button[data-test='" + addAttr.replace("add-to-cart", "remove") + "']";
            scrollIntoView(addBtn);
            addBtn.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(removeSelector)));
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.cssSelector("[data-test='shopping-cart-badge']"),
                    String.valueOf(i + 1)
            ));
        }
    }

    public int getCartBadgeCount() {
        try {
            WebElement badge = driver.findElement(By.cssSelector("[data-test='shopping-cart-badge']"));
            return Integer.parseInt(badge.getText().trim());
        } catch (NoSuchElementException e) {
            return 0;
        }
    }
}