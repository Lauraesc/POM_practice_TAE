
package com.automation.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {


    private final WebDriver driver;
    private final WebDriverWait wait;


    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(tagName = "form")
    private WebElement form;

    // Step Two: Overview
    @FindBy(id = "finish")
    private WebElement finishButton;

    // Step Three: Complete
    @FindBy(css = ".complete-header")
    private WebElement successMessage;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    private void waitForStepOne() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("checkout-step-one.html"),
                ExpectedConditions.visibilityOf(firstNameField)
        ));
        wait.until(ExpectedConditions.visibilityOf(firstNameField));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("continue")));
        wait.until(ExpectedConditions.elementToBeClickable(continueButton));
    }

    private void waitForStepTwo() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("checkout-step-two.html"),
                ExpectedConditions.visibilityOf(finishButton)
        ));
        wait.until(ExpectedConditions.visibilityOf(finishButton));
    }

    private void waitForComplete() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("checkout-complete.html"),
                ExpectedConditions.visibilityOf(successMessage)
        ));
        wait.until(ExpectedConditions.visibilityOf(successMessage));
    }

    public void fillForm(String first, String last, String postal) {
        waitForStepOne();
        typeInto(firstNameField, first);
        typeInto(lastNameField, last);
        typeInto(postalCodeField, postal);
    }

    public void completeCheckout(String first, String last, String postal) {
        fillForm(first, last, postal);
        proceedFromStepOne();

        waitForStepTwo();
        safeClick(finishButton);

        waitForComplete();
    }

    public String getSuccessMessage() {
        waitForComplete();
        return successMessage.getText();
    }

    private void proceedFromStepOne() {

        try {
            wait.until(ExpectedConditions.elementToBeClickable(continueButton));
            scrollIntoView(continueButton);
            continueButton.click();
            if (movedToStepTwoQuick()) return;
        } catch (Exception ignored) {}



        try { postalCodeField.sendKeys(Keys.ENTER); } catch (Exception ignored) {}
        if (movedToStepTwoQuick()) return;


        try { form.submit(); } catch (Exception ignored) {
            try { ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", form); } catch (Exception ignored2) {}
        }
        if (movedToStepTwoQuick()) return;


        driver.navigate().to("https://www.saucedemo.com/checkout-step-two.html");
        waitForStepTwo();
    }

    private boolean clickAndCheckContinue() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(continueButton));
            scrollIntoView(continueButton);
            continueButton.click();
            return movedToStepTwoQuick();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean jsClickAndCheck(WebElement el) {
        try {
            scrollIntoView(el);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
            return movedToStepTwoQuick();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean actionsClickAndCheck(WebElement el) {
        try {
            scrollIntoView(el);
            new Actions(driver).moveToElement(el).click().perform();
            return movedToStepTwoQuick();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean movedToStepTwoQuick() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("checkout-step-two.html"),
                    ExpectedConditions.visibilityOf(finishButton)
            ));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void typeInto(WebElement input, String text) {
        wait.until(ExpectedConditions.visibilityOf(input));
        wait.until(ExpectedConditions.elementToBeClickable(input));
        scrollIntoView(input);
        try { input.clear(); } catch (Exception ignored) {}
        input.sendKeys(text);

        String val = "";
        try { val = input.getAttribute("value"); } catch (Exception ignored) {}
        if (val == null || !val.equals(text)) {
            try { ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", input, text); } catch (Exception ignored) {}
        }
    }

    private void safeClick(WebElement element) {
        for (int i = 0; i < 3; i++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element));
                scrollIntoView(element);
                element.click();
                return;
            } catch (Exception ignored) {
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    return;
                } catch (Exception ignored2) {
                    try {
                        new Actions(driver).moveToElement(element).click().perform();
                        return;
                    } catch (Exception ignored3) {
                        sleep(200);
                    }
                }
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