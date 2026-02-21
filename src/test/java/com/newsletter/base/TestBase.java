package com.newsletter.base;


import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

// Single-responsibility: test setup/teardown, common utilities for tests.
public abstract class TestBase {

    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        //create a driver through the driver factory
        driver = DriverFactory.createChromeDriver();
        driver.manage().window().maximize(); //maximize the window
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);// implicit waits are avoided to prevent hidden timing issues
    }

    @AfterEach
    public void tearDown() {
        // attach a final screenshot for post-test inspection in Allure

        try {
            if (driver instanceof TakesScreenshot) {
                byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.getLifecycle().addAttachment("Final screenshot", "image/png", "png", png);
            }
        } catch (Exception ignored) {
            // Best-effort only; never block teardown
        }

        // Always quit to release resources
        if (driver != null) {
            driver.quit();
        }
    }

    // Small helper to attach plain text info to Allure (e.g., environment, URL)
    protected void attachInfo(String name, String content) {
        Allure.getLifecycle().addAttachment(name, "text/plain", "txt", content.getBytes(StandardCharsets.UTF_8));
    }

}



