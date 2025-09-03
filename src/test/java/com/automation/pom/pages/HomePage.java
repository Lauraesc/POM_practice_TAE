package com.automation.pom.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    private WebDriver driver;


    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;


    @FindBy(css = ".inventory_item:first-of-type button")
    private WebElement firstProductAddButton;


    @FindBy(className = "shopping_cart_link")
    private WebElement cartIcon;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void addFirstProductToCart() {
        firstProductAddButton.click();
    }

    public void openCart() {
        cartIcon.click();
    }

    public void logout() {
        menuButton.click();
        logoutLink.click();
    }
}
