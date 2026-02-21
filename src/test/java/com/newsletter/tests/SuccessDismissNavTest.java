package com.newsletter.tests;


import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import com.newsletter.base.TestBase;
import com.newsletter.pages.NewsLetterSignUpPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Single responsibility: From success card, clicking Dismiss returns to the sign-up form.
 */
@Epic("Newsletter")
@Feature("Signup Form")
@Story("Navigation: Dismiss success returns to form")
@Severity(SeverityLevel.CRITICAL)



public class SuccessDismissNavTest extends TestBase {


    private static final String BASE_URL = "https://news-letter-signup-page.netlify.app/";

    @Test
    @Description("After a valid submission, click Dismiss and verify the form is visible and success card is hidden.")
    void dismiss_fromSuccess_returnsToForm() {
        String email = "test@example.com";

        NewsLetterSignUpPage page = new NewsLetterSignUpPage(driver)
                .open(BASE_URL)
                .enterEmail(email)
                .clickSubscribe();

        // Ensure we are on success first
        page.waitForSuccessVisible();
        assertThat(page.isSuccessVisible()).as("Precondition: success visible").isTrue();

        // Click Dismiss and verify we are back on the form
        page.clickDismiss();
        page.waitForFormVisible();

        assertThat(page.isFormVisible()).as("Form visible after dismiss").isTrue();
        assertThat(page.isSuccessVisible()).as("Success hidden after dismiss").isFalse();
    }

}
