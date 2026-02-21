package com.newsletter.pages;



import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


import java.time.Duration;
import java.util.List;


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





}
