package ua.kpi.tzyapz.shopline.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest extends BaseUiTest {

    private static final String LOGIN_URL = BASE_URL + "auth/login";
    private static final String SAFE_CUSTOMER_EMAIL = "customer2@practicesoftwaretesting.com";
    private static final String NON_EXISTENT_EMAIL = "nonexistent.fb46@example.com";

    private static final By EMAIL_INPUT = By.cssSelector("[data-test='email']");
    private static final By PASSWORD_INPUT = By.cssSelector("[data-test='password']");
    private static final By LOGIN_SUBMIT = By.cssSelector("[data-test='login-submit']");
    private static final By PAGE_TITLE = By.cssSelector("[data-test='page-title']");
    private static final By NAV_MENU = By.cssSelector("[data-test='nav-menu']");
    private static final By NAV_SIGN_OUT = By.cssSelector("[data-test='nav-sign-out']");
    private static final By NAV_SIGN_IN = By.cssSelector("[data-test='nav-sign-in']");
    private static final By LOGIN_ERROR = By.cssSelector("[data-test='login-error']");

    @Test
    @DisplayName("TC-005 / F-20: успішний вхід і вихід покупця")
    void successfulLoginAndLogout() {
        driver.get(LOGIN_URL);

        wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_INPUT)).sendKeys(SAFE_CUSTOMER_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(CUSTOMER_PASSWORD);
        driver.findElement(LOGIN_SUBMIT).click();

        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
        WebElement navMenu = wait.until(ExpectedConditions.elementToBeClickable(NAV_MENU));

        assertAll("Успішний вхід",
                () -> assertEquals("My account", title.getText().trim()),
                () -> assertTrue(navMenu.isDisplayed())
        );

        navMenu.click();
        wait.until(ExpectedConditions.elementToBeClickable(NAV_SIGN_OUT)).click();

        WebElement signInLink = wait.until(ExpectedConditions.visibilityOfElementLocated(NAV_SIGN_IN));
        assertTrue(signInLink.isDisplayed(), "Після виходу має з'явитися кнопка Sign in");
    }

    @Test
    @DisplayName("TC-006 / F-20, GUI-60: вхід із неправильним паролем")
    void loginWithInvalidPasswordShowsError() {
        driver.get(LOGIN_URL);

        wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_INPUT)).sendKeys(NON_EXISTENT_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys("wrongpass123");
        driver.findElement(LOGIN_SUBMIT).click();

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_ERROR));

        assertAll("Невірні облікові дані",
                () -> assertTrue(driver.getCurrentUrl().contains("/auth/login"), "Переходу зі сторінки входу не має бути"),
                () -> assertTrue(driver.findElement(LOGIN_SUBMIT).isDisplayed(), "Форма входу має залишитися на екрані"),
                () -> assertEquals("Invalid email or password", error.getText().trim())
        );
    }
}
