package com.newsletter.pages;


import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List

//NewsletterSignupPage — Page Object for the newsletter signup single-page app.
// Single-responsibility: encapsulate UI locators and simple interactions/waits.
public class NewsLetterSignUpPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    //Email input field.
    @FindBy(id = "email")
    private WebElement emailInput;

    //subscribe button
    @FindBy(xpath = "//button[@type='submit']")
    private WebElement subscribeButton;

    //error message element shown for invalid or empty email
    @FindBy(id = "error-message")
    private WebElement errorMessage;

    //success card or thank you message shown after valid submission
    @FindBy(className = "success-message")
    private WebElement successCard;


    // dismiss button in the success card
    @FindBy(xpath = "//*[contains(@class,'success-message')]//button")
    private WebElement dismissButton;

    // The sign-up form container used to verify that the form is visible or hidden
    @FindBy(id = "signup-form") // 1) id for form container
    private WebElement formContainer;

    //The success heading with "Thanks for subscribing!" inside success card.
    @FindBy(xpath = "//*[contains(@class,'success-card')]//*[self::h1 or self::h2 or contains(@class,'title')]")
    private List<WebElement> successTitles;

    // 8) The echoed email shown with a class (e.g., "user-email" / "displayed-email").
    @FindBy(xpath = "//*[contains(@class,'success-card')]//*[contains(@class,'user-email') or contains(@class,'displayed-email') or self::strong]")
    private List<WebElement> emailEchoes;


    public NewsletterSignupPage(WebDriver driver) {
        this.driver = driver;
        // Use short, explicit waits where interactions expect quick UI transitions
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(driver, this);
    }



    @Step("Open sign-up page: {url}")
    public NewsletterSignupPage open(String url) {
        driver.get(url);
        return this;
    }



    @Step("Enter email: {email}")
    public NewsletterSignupPage enterEmail(String email) {
        // Wait until input is visible (inline safe wait to avoid test-scoped utils dependency)
        wait.until(d -> isDisplayedNoThrow(emailInput));
        emailInput.clear();       // ensure clean state
        emailInput.sendKeys(email);
        return this;
    }



    @Step("Click Subscribe")
    public NewsletterSignupPage clickSubscribe() {
        // Wait until button is visible & enabled (inline safe wait)
        wait.until(d -> isDisplayedNoThrow(subscribeButton) && isEnabledNoThrow(subscribeButton));
        subscribeButton.click();
        return this;
    }



    @Step("Click Dismiss message")
    public NewsletterSignupPage clickDismiss() {
        // Wait until dismiss is clickable (visible & enabled)
        wait.until(d -> isDisplayedNoThrow(dismissButton) && isEnabledNoThrow(dismissButton));
        dismissButton.click();
        return this;
    }


    // Visibility / State checks


    @Step("Is success state visible?")
    public boolean isSuccessVisible() {
        return isDisplayedNoThrow(successCard);// WHAT: check card shown; WHY: confirm transition
    }


    @Step("Is form visible?")
    public boolean isFormVisible() {
        return isDisplayedNoThrow(formContainer); // WHAT: check form shown; WHY: confirm initial/returned state
    }

    @Step("Is error message visible?")
    public boolean isErrorVisible() {
        return isDisplayedNoThrow(errorMessage); // WHAT: inline validation visible; WHY: invalid/empty input behavior
    }

    @Step("Get error message text")
    public String getErrorText() {
        return isErrorVisible() ? safeGetText(errorMessage) : "";
    }

    // Try title first if present; otherwise fall back to entire card's visible text
    @Step("Get success message text (title/body)")
    public String getSuccessText() {
        // Prefer a header/title
        if (successTitles != null) {
            for (WebElement e : successTitles) {
                if (isDisplayedNoThrow(e)) {
                    String t = safeGetText(e);
                    if (!t.isEmpty()) return t;
                }
            }
        }
        // Fallback to full card text
        return isDisplayedNoThrow(successCard) ? safeGetText(successCard) : "";
    }



    @Step("Get displayed email inside success card")
    public String getDisplayedEmailOnSuccess() {
        if (emailEchoes != null) {
            for (WebElement e : emailEchoes) {
                if (isDisplayedNoThrow(e)) {
                    String t = safeGetText(e);
                    if (!t.isEmpty() && t.contains("@")) return t;
                }
            }
        }
        // Fallback heuristic: find first token with '@' in the success text
        String all = getSuccessText();
        for (String token : all.split("\\s+")) {
            if (token.contains("@")) return token.replaceAll("[,;.!?)]*$", "");
        }
        return "";
    }



    @Step("Wait for success state to be visible")
    public void waitForSuccessVisible() {
        wait.until(d -> isDisplayedNoThrow(successCard));
    }

    @Step("Wait until form is visible")
    public void waitForFormVisible() {
        wait.until(d -> isDisplayedNoThrow(formContainer));
    }

    @Step("Wait for inline error to be visible")
    public void waitForErrorVisible() {
        wait.until(d -> isDisplayedNoThrow(errorMessage));
    }

//local safe checks



    private boolean isDisplayedNoThrow(WebElement element) {
        try {
            return element != null && element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | NullPointerException e) {
            return false;
        }
    }

    private boolean isEnabledNoThrow(WebElement element) {
        try {
            return element != null && element.isEnabled();
        } catch (StaleElementReferenceException | NullPointerException e) {
            return false;
        }
    }

    private String safeGetText(WebElement element) {
        try {
            return element.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }


}
