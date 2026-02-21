package com.newsletter.tests;


import io.qameta.allure.*;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import com.newsletter.base.TestBase;
import com.newsletter.pages.NewsLetterSignUpPage;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Single responsibility: Validate end-to-end happy path:
 * - Submit valid email
 * - Success state appears with correct message and echoed email
 * - Dismiss and return to form
 */


@Epic("Newsletter")
@Feature("Signup Form")
@Story("Happy path submission")
@Severity(SeverityLevel.CRITICAL)


public class HappyPathSignUpTest extends TestBase {


    private static final String BASE_URL = "https://news-letter-signup-page.netlify.app/";

    @Test
    @Description("Submit a valid email, verify success message contains text and email, dismiss, then return to form.")
    void happyPath_fullCycle() throws Exception {
        String email = "test@example.com"; // deterministic test data

        // Orchestrate user flow using Page Object
        NewsLetterSignUpPage page = new NewsLetterSignUpPage(driver)
                .open(BASE_URL)           // open page
                .enterEmail(email)        // type valid email
                .clickSubscribe();        // submit

        // Wait for and verify success state
        page.waitForSuccessVisible();

        String successText = page.getSuccessText();
        String echoedEmail = page.getDisplayedEmailOnSuccess();

        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(page.isSuccessVisible())
                    .as("Success card should be visible")
                    .isTrue();

            softly.assertThat(successText)
                    .as("Success message should contain 'Thanks for subscribing!'")
                    .containsIgnoringCase("Thanks for subscribing!");

            softly.assertThat(echoedEmail)
                    .as("Success card should display the submitted email")
                    .contains(email);
        }

        // Dismiss and verify we returned to the form
        page.clickDismiss();
        page.waitForFormVisible();

        assertThat(page.isFormVisible()).as("Form visible after dismiss").isTrue();
        assertThat(page.isSuccessVisible()).as("Success hidden after dismiss").isFalse();

    }
}




