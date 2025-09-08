package com.automation.pom.tests;

import com.automation.pom.pages.CartPage;
import com.automation.pom.pages.HomePage;
import com.automation.pom.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RemoveItemsTest extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;
    private CartPage cartPage;

    @BeforeMethod
    public void startFresh() {
        driver.manage().deleteAllCookies();
        driver.get(LoginPage.LOGIN_URL);
        loginPage = new LoginPage(driver);
        homePage = loginPage.loginAs("standard_user", "secret_sauce");
    }

    @Test
    public void addThreeItems_thenRemoveAll_cartMustBeEmpty() {
        homePage.addItems(3);
        Assert.assertEquals(homePage.getCartBadgeCount(), 3, "The cart badge should show 3 items");
        homePage.goToCart();
        cartPage = new CartPage(driver);
        Assert.assertEquals(cartPage.getCartItemCount(), 3, "The cart should contain 3 items");
        cartPage.removeAllItems();
        Assert.assertTrue(cartPage.isEmpty(), "The cart should be empty after removing all items");
        Assert.assertTrue(cartPage.isBadgeAbsent(), "The badge should be absent after removing all items");
    }
}