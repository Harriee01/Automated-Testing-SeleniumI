package com.newsletter.base;


import io.github.bonigarcia.wdm.WebDriverManager; // WebDriverManager to handle driver binaries
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;



// Single-responsibility: this class ONLY creates WebDriver instances
// and applies environment-based options.
public final class DriverFactory {


    private DriverFactory() {
        // prevent instantiation
    }


    // Factory method to build a WebDriver ready for use
    public static WebDriver createChromeDriver() {
        // Always rely on WebDriverManager so that there's no need to manually download drivers
        WebDriverManager.chromedriver().setup();

        // Build ChromeOptions once to keep driver creation concise and consistent
        ChromeOptions options = new ChromeOptions();

        // Headless mode is activated only in CI
        // This will be set automatically to "true"
        boolean headless = Boolean.parseBoolean(System.getenv().getOrDefault("HEADLESS", "false"));
        if (headless) {
            options.addArguments("--headless=new"); // new headless mode is more stable in recent Chrome
            options.addArguments("--window-size=1920,1080"); // ensure consistent layout without a real display
        }

        // Good stability flags in CI
        options.addArguments("--disable-gpu"); // does nothing on many OS but safe
        options.addArguments("--no-sandbox");  // useful in containerized CI
        options.addArguments("--disable-dev-shm-usage"); // prevent shared memory issues in CI

        // Return a fresh ChromeDriver instance
        return new ChromeDriver(options);
    }


}
