package com.newsletter.tests;


import io.qameta.allure.*;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.*;
import com.newsletter.base.TestBase;
import com.newsletter.pages.NewsLetterSignUpPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * One class containing the four required scenarios.
 * - We keep tests independent and readable.
 * - Allure annotations on class and methods for rich reporting.
 * - AssertJ for fluent, descriptive assertions.
 */
@Epic("Newsletter")
@Feature("Signup Form")
@Severity(SeverityLevel.CRITICAL)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NewsletterSignupTest extends TestBase {

    private static final String BASE_URL = "https://news-letter-signup-page.netlify.app/";

    @Test
    @Order(1)
    @Story("Happy path: submit valid email and dismiss success")
    @Description("Open page, submit valid email, verify success message contains the email, dismiss, then return to form.")
    void happyPath_fullCycle() throws Exception {
        String email = "test@example.com"; // deterministic test data

        NewsLetterSignUpPage page = new NewsLetterSignUpPage(driver)
                .open(BASE_URL)           // 1) open
                .enterEmail(email)        // 2) type valid email
                .clickSubscribe();        // 3) submit

        page.waitForSuccessVisible();     // 4) wait SPA transition to success

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
                    .as("Success screen should display the submitted email")
                    .contains(email);
        }

        // 6) Dismiss and confirm return to form
        page.clickDismiss();

        // 7) Verify form visible, success hidden
        page.waitForFormVisible();
        assertThat(page.isFormVisible()).as("Form visible after dismiss").isTrue();
        assertThat(page.isSuccessVisible()).as("Success hidden after dismiss").isFalse();

        // 8) Small pause only for local visual confirmation (avoid in CI)
        Thread.sleep(1000);
    }

    @Test
    @Order(2)
    @Story("Validation: invalid email shows inline error, no success")
    @Severity(SeverityLevel.NORMAL)
    @Description("Enter an invalid email (no @), submit, verify inline error text and no success state.")
    void invalidEmail_showsError_noSuccess() {
        String invalidEmail = "invalid-email";

        NewsLetterSignUpPage page = new NewsLetterSignUpPage(driver)
                .open(BASE_URL)               // 1) open
                .enterEmail(invalidEmail)     // 2) invalid email
                .clickSubscribe();            // 3) submit

        page.waitForErrorVisible();           // 4) wait inline error
        assertThat(page.getErrorText())
                .as("Inline error should indicate a valid email is required")
                .containsIgnoringCase("valid email");

        // 5) Ensure success not shown
        assertThat(page.isSuccessVisible())
                .as("Success card should not be visible after invalid email")
                .isFalse();
    }

    @Test
    @Order(3)
    @Story("Validation: empty email shows same inline error")
    @Severity(SeverityLevel.NORMAL)
    @Description("Leave email empty, submit, verify inline error text is displayed and no success state.")
    void emptyEmail_showsError_noSuccess() {
        NewsLetterSignUpPage page = new NewsLetterSignUpPage(driver)
                .open(BASE_URL) // 1) open
                // 2) leave empty on purpose
                .clickSubscribe(); // 3) submit

        page.waitForErrorVisible(); // 4) wait inline error
        assertThat(page.getErrorText())
                .as("Inline error should indicate a valid email is required")
                .containsIgnoringCase("valid email");

        assertThat(page.isSuccessVisible())
                .as("Success card should not appear when email is empty")
                .isFalse();
    }

    @Test
    @Order(4)
    @Story("Navigation: Dismiss success returns to form")
    @Severity(SeverityLevel.CRITICAL)
    @Description("After a valid submission, click Dismiss and verify the form is visible and success card is hidden.")
    void dismiss_fromSuccess_returnsToForm() {
        String email = "test@example.com";

        NewsLetterSignUpPage page = new NewsLetterSignUpPage(driver)
                .open(BASE_URL)
                .enterEmail(email)
                .clickSubscribe();

        // Precondition: ensure success visible before we click dismiss
        page.waitForSuccessVisible();
        assertThat(page.isSuccessVisible())
                .as("Precondition: success should be visible before dismiss")
                .isTrue();

        // Action: Dismiss
        page.clickDismiss();

        // Expectation: back to form
        page.waitForFormVisible();
        assertThat(page.isFormVisible())
                .as("Form should be visible again after dismiss")
                .isTrue();
        assertThat(page.isSuccessVisible())
                .as("Success card should be hidden after dismiss")
                .isFalse();
    }
}

