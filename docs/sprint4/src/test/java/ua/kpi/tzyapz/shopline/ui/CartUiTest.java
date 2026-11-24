package ua.kpi.tzyapz.shopline.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.refreshed;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

class CartUiTest extends BaseUiTest {

    private static final By CART_LINK = By.cssSelector("[data-test='nav-cart']");
    private static final By PRODUCT_TITLE = By.cssSelector("[data-test='product-title']");
    private static final By QUANTITY = By.cssSelector("[data-test='product-quantity']");
    private static final By LINE_PRICE = By.cssSelector("[data-test='line-price']");
    private static final By CART_TOTAL = By.cssSelector("[data-test='cart-total']");

    @Test
    @DisplayName("TC-018 / F-100, GUI-70: зміна кількості до 5 перераховує рядок і Total")
    void changingCartQuantityUpdatesLineAndTotal() {
        ProductPage.open(driver, wait, BASE_URL);
        BigDecimal unitPrice = readAmount(ProductPage.UNIT_PRICE);
        assertEquals("1", wait.until(visibilityOfElementLocated(ProductPage.QUANTITY))
                .getDomProperty("value"));

        wait.until(elementToBeClickable(ProductPage.ADD_TO_CART)).click();
        wait.until(elementToBeClickable(CART_LINK)).click();

        WebElement quantity = wait.until(elementToBeClickable(QUANTITY));
        assertAll("Кошик після додавання однієї одиниці",
                () -> assertEquals(1, driver.findElements(PRODUCT_TITLE).size()),
                () -> assertEquals(ProductPage.NAME,
                        wait.until(visibilityOfElementLocated(PRODUCT_TITLE)).getText().strip()),
                () -> assertEquals("1", quantity.getDomProperty("value")),
                () -> assertEquals(unitPrice, readAmount(LINE_PRICE)),
                () -> assertEquals(unitPrice, readAmount(CART_TOTAL))
        );

        quantity.click();
        // Підтверджуємо Enter і знімаємо фокус. Toolshop зберігає значення через подію change.
        quantity.sendKeys(Keys.END, Keys.BACK_SPACE, "5", Keys.ENTER, Keys.TAB);

        BigDecimal expectedTotal = unitPrice.multiply(BigDecimal.valueOf(5));
        wait.withMessage("Загальний Total не перераховано до " + expectedTotal)
                .until(refreshed(browser -> {
                    WebElement total = browser.findElement(CART_TOTAL);
                    return total.isDisplayed() && expectedTotal.equals(parseAmount(total.getText()));
                }));

        assertAll("Кошик після зміни кількості",
                () -> assertEquals("5", wait.until(visibilityOfElementLocated(QUANTITY))
                        .getDomProperty("value")),
                () -> assertEquals(expectedTotal, readAmount(LINE_PRICE)),
                () -> assertEquals(expectedTotal, readAmount(CART_TOTAL))
        );
    }

    private BigDecimal readAmount(By locator) {
        return wait.until(refreshed(browser -> {
            WebElement element = browser.findElement(locator);
            return element.isDisplayed() ? parseAmount(element.getText()) : null;
        }));
    }

    private static BigDecimal parseAmount(String text) {
        String amount = text.replace("$", "").replace(",", "").strip();
        if (amount.isEmpty()) {
            return null;
        }
        return new BigDecimal(amount).setScale(2, RoundingMode.UNNECESSARY);
    }
}
