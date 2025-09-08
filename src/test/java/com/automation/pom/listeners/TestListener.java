package com.automation.pom.listeners;

import org.openqa.selenium.*;
import org.testng.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Object instance = result.getInstance();
        WebDriver driver = null;
        try {
            driver = (WebDriver) result.getTestClass().getRealClass()
                    .getDeclaredField("driver")
                    .get(instance);
        } catch (Exception ignore) {}

        if (driver == null) return;
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path dest = Path.of("target", "screenshots",
                    result.getMethod().getMethodName() + "_" + System.currentTimeMillis() + ".png");
            Files.createDirectories(dest.getParent());
            Files.copy(src.toPath(), dest);
            Reporter.log("Screenshot saved to: " + dest.toAbsolutePath(), true);
        } catch (Exception e) {
            Reporter.log("Screenshot failed: " + e.getMessage(), true);
        }
    }
}