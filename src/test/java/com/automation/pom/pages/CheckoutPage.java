package com.automation.pom.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutPage {

    private WebDriver driver;

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
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void fillForm(String first, String last, String postal) {
        firstNameField.sendKeys(first);
        lastNameField.sendKeys(last);
        postalCodeField.sendKeys(postal);
    }

    public void continueCheckout() {
        continueButton.click();
    }

    public void finishCheckout() {
        finishButton.click();
    }

    public String getSuccessMessage() {
        return successMessage.getText();
    }
}
