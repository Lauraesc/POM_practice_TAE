package com.automation.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(driver, this);
    }

    public void logout() {

        System.out.println("👉 Click en menú lateral");
        wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();


        System.out.println("👉 Click en logout");
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();


        System.out.println("👉 Esperando login-button...");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login-button")));
        System.out.println("✅ Login button encontrado!");
    }

}
