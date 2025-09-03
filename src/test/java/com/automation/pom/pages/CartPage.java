package com.automation.pom.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class CartPage {

    private WebDriver driver;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(css = ".cart_button")
    private List<WebElement> removeButtons;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public void clickCheckout() {
        checkoutButton.click();
    }

    public void removeAllItems() {
        for (WebElement btn : removeButtons) {
            btn.click();
        }
    }
}
