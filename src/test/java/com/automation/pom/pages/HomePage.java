package com.automation.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import java.util.Random;

public class HomePage {


    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    private By cartLink = By.cssSelector("[data-test='shopping-cart-link']");
    private By addToCartBtn = By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(driver, this);

        wait.until(ExpectedConditions.urlContains("inventory.html"));
    }


    public void logout() {
        System.out.println("click on menu button");
        menuButton.click();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("click on logout");
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", logoutLink);

        System.out.println("waiting for login button to be present");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login-button")));
        System.out.println("login button is present");
    }


    public void addRandomItemToCart() {
        List<WebElement> addButtons = wait.until(d ->
                d.findElements(By.cssSelector("button[data-test^='add-to-cart-']"))
        );
        if (addButtons == null || addButtons.isEmpty()) {
            throw new IllegalStateException("No se encontraron botones 'Add to cart' en inventory.html");
        }

        int idx = new Random().nextInt(addButtons.size());
        WebElement randomAddBtn = addButtons.get(idx);
        safeClick(randomAddBtn);


        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='shopping-cart-badge']")));
    }


    public void goToCart() {
        boolean navigated = false;

        for (int attempt = 0; attempt < 3 && !navigated; attempt++) {
            try {
                WebElement cart = wait.until(ExpectedConditions.elementToBeClickable(cartLink));
                scrollIntoView(cart);


                cart.click();
                if (waitForCartUrlQuick()) {
                    navigated = true;
                    break;
                }


                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cart);
                if (waitForCartUrlQuick()) {
                    navigated = true;
                    break;
                }


                new Actions(driver).moveToElement(cart).click().perform();
                if (waitForCartUrlQuick()) {
                    navigated = true;
                    break;
                }
            } catch (Exception ignored) {

            }
        }


        if (!navigated) {
            driver.navigate().to("https://www.saucedemo.com/cart.html");
        }


        wait.until(ExpectedConditions.urlContains("cart.html"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkout")));
    }

// Helpers

    private boolean waitForCartUrlQuick() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.urlContains("cart.html"));
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

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    private void scrollIntoView(WebElement el) {
        try {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (Exception ignored) {}
    }
}