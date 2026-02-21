package com.newsletter.tests;


import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import com.newsletter.base.TestBase;
import com.newsletter.pages.NewsLetterSignUpPage;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Single responsibility: Empty email shows the same inline validation error; no success state.
 */
@Epic("Newsletter")
@Feature("Signup Form")
@Story("Validation: Empty email")
@Severity(SeverityLevel.NORMAL)


public class EmptyEmailTest extends TestBase {


    private static final String BASE_URL = "https://news-letter-signup-page.netlify.app/";

    @Test
    @Description("Submit with empty email, expect same inline error and no success state.")
    void emptyEmail_showsError_noSuccess() {
        NewsLetterSignUpPage page = new NewsLetterSignUpPage(driver)
                .open(BASE_URL)
                // intentionally do not type email
                .clickSubscribe();

        page.waitForErrorVisible();

        assertThat(page.getErrorText())
                .as("Error message should indicate a valid email is required")
                .containsIgnoringCase("valid email");

        assertThat(page.isSuccessVisible())
                .as("Success card should not appear for empty input")
                .isFalse();
    }

}
