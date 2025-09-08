package com.automation.pom.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class CheckoutPage extends BasePage {

    private final WebDriverWait wait;

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(css = ".complete-header")
    private WebElement successMessage;

    public CheckoutPage(WebDriver driver) {
        super(driver);
        this.wait  = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        waitForStepOne();
    }

    private void waitForStepOne() {
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/checkout-step-one.html"));
        wait.until(ExpectedConditions.visibilityOf(continueButton));
    }

    public void fillForm(String first, String last, String postal) {
        typeAndVerify(firstNameField, first);
        typeAndVerify(lastNameField,  last);
        typeAndVerify(postalCodeField, postal);
    }

    public void completeCheckout(String first, String last, String postal) {
        fillForm(first, last, postal);
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/checkout-step-two.html"));
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/checkout-complete.html"));
        wait.until(ExpectedConditions.visibilityOf(successMessage));
    }

    public String getSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOf(successMessage));
        return successMessage.getText();
    }

    private void typeAndVerify(WebElement input, String text) {
        wait.until(ExpectedConditions.visibilityOf(input));
        input.clear();
        input.sendKeys(text);
        for (int i = 0; i < 1; i++) {
            String val = input.getAttribute("value");
            if (text.equals(val)) return;
            input.clear();
            input.sendKeys(text);
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", input, text);
    }
}