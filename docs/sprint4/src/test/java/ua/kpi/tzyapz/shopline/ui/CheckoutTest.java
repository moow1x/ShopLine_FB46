package ua.kpi.tzyapz.shopline.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutTest extends BaseUiTest {

    private static final String SAFE_CUSTOMER_EMAIL = "customer2@practicesoftwaretesting.com";

    private static final By COMBINATION_PLIERS = By.xpath(
            "//*[@data-test='product-name' and normalize-space(.)='Combination Pliers']");
    private static final By ADD_TO_CART = By.cssSelector("[data-test='add-to-cart']");
    private static final By CART_QUANTITY = By.cssSelector("[data-test='cart-quantity']");
    private static final By NAV_CART = By.cssSelector("[data-test='nav-cart']");
    private static final By PROCEED_STEP_1 = By.cssSelector("[data-test='proceed-1']");
    private static final By EMAIL_INPUT = By.cssSelector("[data-test='email']");
    private static final By PASSWORD_INPUT = By.cssSelector("[data-test='password']");
    private static final By LOGIN_SUBMIT = By.cssSelector("[data-test='login-submit']");
    private static final By PROCEED_STEP_2 = By.cssSelector("[data-test='proceed-2']");
    private static final By PROCEED_STEP_3 = By.cssSelector("[data-test='proceed-3']");
    private static final By STREET_INPUT = By.cssSelector("[data-test='street']");

    @Test
    @DisplayName("TC-019 / F-110, F-20: вхід під час Checkout (крок 2) і перехід на Billing Address")
    void loginDuringCheckoutRedirectsToBillingAddress() {
        driver.get(BASE_URL);

        wait.until(ExpectedConditions.elementToBeClickable(COMBINATION_PLIERS)).click();
        wait.until(ExpectedConditions.elementToBeClickable(ADD_TO_CART)).click();
        wait.until(ExpectedConditions.textToBe(CART_QUANTITY, "1"));

        wait.until(ExpectedConditions.elementToBeClickable(NAV_CART)).click();
        wait.until(ExpectedConditions.elementToBeClickable(PROCEED_STEP_1)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_INPUT)).sendKeys(SAFE_CUSTOMER_EMAIL);
        driver.findElement(PASSWORD_INPUT).sendKeys(CUSTOMER_PASSWORD);
        driver.findElement(LOGIN_SUBMIT).click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(PROCEED_STEP_2),
                ExpectedConditions.visibilityOfElementLocated(PROCEED_STEP_3)
        ));

        List<WebElement> proceedStep2 = driver.findElements(PROCEED_STEP_2);
        if (!proceedStep2.isEmpty() && proceedStep2.get(0).isDisplayed()) {
            wait.until(ExpectedConditions.elementToBeClickable(PROCEED_STEP_2)).click();
        }

        WebElement proceedStep3 = wait.until(ExpectedConditions.visibilityOfElementLocated(PROCEED_STEP_3));
        WebElement streetInput = wait.until(ExpectedConditions.visibilityOfElementLocated(STREET_INPUT));

        assertAll("Перехід на крок 3 (Billing Address)",
                () -> assertTrue(streetInput.isDisplayed(), "Форма Billing Address не відображається"),
                () -> assertTrue(proceedStep3.isDisplayed(), "Кнопка переходу з кроку 3 не відображається")
        );
    }
}
