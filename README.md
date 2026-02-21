# Newsletter Signup — Selenium Test Automation Suite

> A professional-grade QA automation suite demonstrating Selenium WebDriver,
> JUnit 5, Page Object Model, Allure reporting, and GitHub Actions CI.

---

## Table of Contents

1. [Project Objectives](#project-objectives)
2. [Application Under Test](#application-under-test)
3. [Prerequisites](#prerequisites)
4. [Project Structure](#project-structure)
5. [Running Tests Locally](#running-tests-locally)
6. [Viewing the Allure Report](#viewing-the-allure-report)
7. [Test Cases](#test-cases)
8. [Design Principles](#design-principles)
9. [Locator Strategy](#locator-strategy)
10. [SOLID Adherence Summary](#solid-adherence-summary)
11. [CI/CD with GitHub Actions](#cicd-with-github-actions)
12. [Troubleshooting](#troubleshooting)

---

## Project Objectives

This lab demonstrates:

- **Selenium WebDriver 4** with Java for browser automation
- **JUnit 5** as the test framework with proper lifecycle annotations
- **Page Object Model (POM)** with Page Factory for clean separation of concerns
- **WebDriverManager** for automatic ChromeDriver binary management
- **Explicit waits** (`WebDriverWait`) for robust, flake-resistant tests
- **AssertJ** for fluent, readable assertions
- **Allure** for rich, visual test reports with steps and annotations
- **GitHub Actions** for Continuous Integration with headless Chrome

---

## Application Under Test

**URL:** https://news-letter-signup-page.netlify.app/

A single-page newsletter sign-up form (Frontend Mentor challenge):

- Submitting a **valid email** hides the form and shows a success/thank-you card (no page reload)
- The success card contains a personalised message with the submitted email
- Clicking **"Dismiss message"** restores the form
- Submitting **invalid** or **empty** email shows an inline validation error

---

## Prerequisites

| Tool | Version       | Install |
|------|---------------|---------|
| Java (JDK) | 21 or higher  | https://adoptium.net |
| Maven | 4+            | https://maven.apache.org |
| Google Chrome | Latest stable | https://www.google.com/chrome |
| Git | Any recent    | https://git-scm.com |

Verify your setup:

```bash
java -version   # Should show 17+
mvn -version    # Should show 3.8+
```

> **ChromeDriver?** You do NOT need to install it manually.
> WebDriverManager downloads the correct version automatically at runtime.

---

## Project Structure

```
newsletter-signup-tests/
├── .github/
│   └── workflows/
│       └── ci.yml
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/harriet/pages/
│   │            └── NewsletterSignupPage.java   ← Page Object (source code layer)
│   └── test/
│       ├── java/
│       │   └── com/harriet/
│       │       ├── base/
│       │       │   └── BaseTest.java           ← WebDriver setup/teardown
│       │       ├── utils/
│       │       │   └── WaitUtils.java          ← Test-only utility waits
│       │       └── tests/
│       │           └── NewsletterSignupTest.java ← All 4 test cases combined here
│       └── resources/
│           └── allure.properties
├── pom.xml
└── README.md
```

### Package responsibilities at a glance

| Package | Responsibility                                   |
|---------|--------------------------------------------------|
| `pages` | Page Object for the newsletter form (locators + actions + inline waits)        |
| `base`  | WebDriver lifecycle, window size, screenshot attachment |
| `tests` |     All test scenarios & all assertions                                                    |
| `utils` | Optional test-only helpers (WaitUtils)           |


---

## Running Tests Locally

### Clone the project

```bash
git clone https://github.com/Harriee01/Automated-Testing-SeleniumI.git
cd newsletter-signup-tests
```

### Run all tests

```bash
mvn clean test
```

This will:

1. Download all Maven dependencies (first run only — then cached)
2. WebDriverManager downloads the correct ChromeDriver
3. Open a **real Chrome browser window** (not headless — you'll see it!)
4. Execute all 3 test cases
5. Write Allure results to `target/allure-results/`

### Run a single test by name

```bash
mvn clean test -Dtest=NewsletterSignupTest#testHappyPathSubscriptionAndDismiss
```

### Run tests matching a pattern

```bash
mvn clean test -Dtest="NewsletterSignupTest#test*Error"
```

---

## Viewing the Allure Report

After running `mvn clean test`, a `target/allure-results/` folder is created.

### Option A — Live server (recommended)

```bash
mvn allure:serve
```

This generates the HTML report and **automatically opens it in your browser**.
Use this during development to review results instantly.

### Option B — Generate static HTML

```bash
mvn allure:report
```

The report is saved to `target/site/allure-maven-plugin/`.
Open `target/site/allure-maven-plugin/index.html` in any browser.

### What you'll see in the Allure report

- **Overview** dashboard — pass/fail counts, duration, trends
- **Suites** — each test class and method with step-by-step breakdown
- **Behaviors** — tests grouped by Epic → Feature → Story (via annotations)
- **Test steps** — each `@Step` method from the page object shown in order
- **Severity levels** — Critical (TC01), Normal (TC02, TC03)

---

## Test Cases

### TC01 — Happy Path: Valid Email Subscription and Dismiss

**Severity:** Critical  
**Story:** Successful Subscription

| # | Action | Expected Result |
|---|--------|-----------------|
| 1 | Open page | Sign-up form visible |
| 2 | Enter `test@example.com` | Email typed into field |
| 3 | Click Subscribe | — |
| 4 | Observe page state | **Success card visible**, form hidden |
| 5 | Read success message | Contains "Thanks for subscribing!" and the email |
| 6 | Click Dismiss | — |
| 7 | Observe page state | **Form restored**, success card hidden |

---

### TC02 — Invalid Email Shows Validation Error

**Severity:** Normal  
**Story:** Form Validation

| # | Action | Expected Result |
|---|--------|-----------------|
| 1 | Open page | Sign-up form visible |
| 2 | Enter `invalid-email` | Text typed |
| 3 | Click Subscribe | — |
| 4 | Observe error | **Error message visible** with "Valid email required" |
| 5 | Check success card | **Not visible** |

---

### TC03 — Empty Email Shows Validation Error

**Severity:** Normal  
**Story:** Form Validation

| # | Action | Expected Result |
|---|--------|-----------------|
| 1 | Open page | Sign-up form visible |
| 2 | Leave email empty | — |
| 3 | Click Subscribe | — |
| 4 | Observe error | **Same error message** visible |
| 5 | Check success card | **Not visible** |


### TC04 — Dismiss Button Returns to Sign‑Up Form

**Severity:** Critical
**Story:** Navigation / Success Card Dismissal

| # | Action | Expected Result                   |
|---|--------|-----------------------------------|
| 1 | Open page | Sign-up form visible              |
| 2 | Enter a valid email (e.g., test@example.com) | Email typed into field            |
| 3 | Click Subscribe | —                                 |
| 4 | Observe page state | Success card visible, form hidden |
| 5 | Click “Dismiss message” | -                                 |
| 6 |          Observe page state               |           Form visible again, success card hidden                |
---

## Design Principles

### Page Object Model (POM) with Page Factory

Every UI interaction is encapsulated in `NewsletterSignupPage.java`.  
Test methods call **human-readable action methods** like `signupPage.enterEmail("x")` rather than raw Selenium calls.  
This means if the app's HTML changes, you update **one page class** — not every test.

### Explicit Waits — No `Thread.sleep`

All synchronisation uses `WebDriverWait` with `ExpectedConditions`:


## CI/CD with GitHub Actions

### Triggers

- **Push to `main`** — runs the full test suite immediately
- **Pull Request to `main`** — runs tests before merge is allowed






---





### Pipeline Steps

```
1. Checkout code
2. Set up JDK 17 (Temurin/OpenJDK)
3. Cache Maven dependencies (keyed on pom.xml hash)
4. Install Google Chrome (stable)
5. mvn clean test  (CI=true → headless Chrome, no display needed)
6. Upload allure-results artifact (even on failure)
7. Generate Allure HTML report
8. Upload Allure HTML report artifact
```

### Viewing results in GitHub

1. Go to your repo → **Actions** tab
2. Click the latest workflow run
3. Scroll down to **Artifacts**
4. Download `allure-html-report` → unzip → open `index.html`

### Slack notifications

The `ci.yml` includes a commented-out Slack notification step.  
To enable it:

1. Create an incoming webhook in your Slack workspace
2. Add it as a GitHub Secret named `SLACK_WEBHOOK_URL`
3. Uncomment the notification step in `ci.yml`

---

## Troubleshooting

### `ChromeDriver` version mismatch error

WebDriverManager should handle this automatically. If you see a mismatch:

```powershell
# Windows PowerShell — clear WebDriverManager cache
Remove-Item -Recurse -Force "$env:USERPROFILE\.m2\repository\org\seleniumhq\selenium"
mvn clean test
```

### `NoSuchElementException` — element not found

This usually means the locator doesn't match the actual HTML.  
**Debug steps:**

1. Open Chrome DevTools (F12) on https://news-letter-signup-page.netlify.app/
2. Inspect the element and check its actual `id`, `class`, or attributes
3. Update the `@FindBy` annotation in `NewsletterSignupPage.java` to match

### `TimeoutException` — wait expired

The explicit wait couldn't find the element within 10 seconds. This means the locator is wrong.  
Trigger the action manually in the browser, inspect the element in DevTools, and verify the `@FindBy` locator matches the actual HTML attribute exactly.

### Tests pass locally but fail in CI

Check for:

- **Timing issues** — the explicit wait timeout may be too short. Try increasing `WAIT_TIMEOUT` in `NewsletterSignupPage`.
- **Window size** — headless Chrome defaults to a small viewport. The `--window-size=1920,1080` arg in `BaseTest` should handle this.
- **Chrome version** — CI uses the latest stable Chrome. If WebDriverManager fails, check the GitHub Actions run log.

### Allure report not generating

Ensure the `target/allure-results/` directory exists after `mvn test`.  
If empty, check that the Allure Maven plugin is present in `pom.xml` and run:

```bash
mvn allure:serve
```

---

## Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 21      | Language |
| Selenium WebDriver | 4.17.0  | Browser automation |
| WebDriverManager | 5.8.0   | Auto ChromeDriver management |
| JUnit 5 | 5.10.2  | Test framework |
| AssertJ | 3.25.3  | Fluent assertions |
| Allure JUnit5 | 2.24.0  | Test reporting |
| AspectJ Weaver | 1.9.21  | Allure @Step instrumentation |
| Maven Surefire | 3.2.5   | Test runner |
| GitHub Actions | —       | CI/CD |

---


