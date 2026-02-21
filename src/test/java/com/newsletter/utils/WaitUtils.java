package com.newsletter.utils;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

//Test-scoped wait helpers.
public final class WaitUtils {

    private WaitUtils() {
        // utility class
    }

    /**
     * Wait until the element is displayed, then return it.
     * @param wait a WebDriverWait configured by caller
     * @param element the element to wait for
     * @return the same WebElement reference
     */
    public static WebElement waitUntilVisible(WebDriverWait wait, WebElement element) {
        wait.until((ExpectedCondition<Boolean>) d -> isDisplayedSafe(element));
        return element;
    }

    /**
     * Wait until the element is visible and enabled (clickable), then return it.
     * @param wait a WebDriverWait configured by caller
     * @param element the element to wait for
     * @return the same WebElement reference
     */
    public static WebElement waitUntilClickable(WebDriverWait wait, WebElement element) {
        waitUntilVisible(wait, element);
        wait.until((ExpectedCondition<Boolean>) d -> element != null && element.isEnabled());
        return element;
    }

    /**
     * Safe isDisplayed check that shields NoSuchElement and StaleElement exceptions.
     */
    public static boolean isDisplayedSafe(WebElement element) {
        try {
            return element != null && element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | NullPointerException e) {
            return false;
        }
    }
}

