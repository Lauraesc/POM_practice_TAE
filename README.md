# SauceDemo — Selenium + Java + TestNG (POM)

Tests Running Evidence
https://youtu.be/UtJ7WYqydC4?si=gKu2HL2olT_yvo8X

## 1. Purpose
1. Automate https://www.saucedemo.com using Selenium 4, TestNG, Maven, and Page Object Model (PageFactory).
2. Use WebDriverManager (no hardcoded driver paths).
3. Cover purchase, remove-items, and logout scenarios.

## 2. Requirements
1. Java 17+
2. Maven 3.9+
3. Internet access

## 3. How to Run (IDE)
1. Open the project as a Maven project.
2. Run src/test/resources/testng.xml.

## 4. How to Run (CLI)
1. mvn clean test
2. Single test class: mvn -Dtest=PurchaseTest test
3. Reports location: target/surefire-reports/

## 5. Scenarios
1. Purchase: pick a random item → add to cart → complete checkout → verify “Thank you for your order!”.
2. Remove items: add 3 items → open cart → remove all → verify cart is empty and badge is gone.
3. Logout: log out → verify redirection to login page.

## 6. Project Layout

src/test/java/com/automation/pom/
pages/ (BasePage, LoginPage, HomePage, CartPage, CheckoutPage)
tests/ (BaseTest, LoginTest, LogoutTest, PurchaseTest, RemoveItemsTest)
listeners/ (TestListener)
src/test/resources/testng.xml

## 7. Design Notes
1. BaseTest creates ChromeDriver with ChromeOptions (disables password/translation popups, runs incognito).
2. Pages use @FindBy + PageFactory and explicit waits (URL, visibility, clickability).
3. Selectors prefer data-test attributes for stability.

## 8. Test Data
1. standard_user / secret_sauce
2. locked_out_user / secret_sauce

## 9. Artifacts
1. Screenshots on failure: target/screenshots/
2. Test reports: target/surefire-reports/
