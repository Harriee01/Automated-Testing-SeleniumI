package com.newsletter.tests;


import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import com.newsletter.base.TestBase;
import com.newsletter.pages.NewsLetterSignUpPage;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Single responsibility: Invalid email shows inline validation error; no success state.
 */
@Epic("Newsletter")
@Feature("Signup Form")
@Story("Validation: Invalid email")
@Severity(SeverityLevel.NORMAL)


public class InvalidEmailTest  extends TestBase {


    private static final String BASE_URL = "https://news-letter-signup-page.netlify.app/";

    @Test
    @Description("Enter an invalid email (no @), submit, expect inline error and no success state.")
    void invalidEmail_showsError_noSuccess() {
        String invalidEmail = "invalid-email";

        NewsLetterSignUpPage page = new NewsLetterSignUpPage(driver)
                .open(BASE_URL)
                .enterEmail(invalidEmail)
                .clickSubscribe();

        page.waitForErrorVisible();

        assertThat(page.getErrorText())
                .as("Error message should indicate a valid email is required")
                .containsIgnoringCase("valid email");

        assertThat(page.isSuccessVisible())
                .as("Success card should not appear for invalid input")
                .isFalse();
    }

}
