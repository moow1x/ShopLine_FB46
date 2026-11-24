package ua.kpi.tzyapz.shopline.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

class ProductTest extends BaseUiTest {

    @Test
    @DisplayName("TC-012 / F-80, GUI-30: картка Combination Pliers і початкова кількість 1")
    void productDetailsShowDefaultQuantity() {
        ProductPage.open(driver, wait, BASE_URL);

        WebElement title = wait.until(visibilityOfElementLocated(ProductPage.TITLE));
        WebElement quantity = wait.until(visibilityOfElementLocated(ProductPage.QUANTITY));

        assertAll(
                () -> assertEquals(ProductPage.NAME, title.getText().strip()),
                () -> assertEquals("number", quantity.getDomProperty("type")),
                () -> assertEquals("1", quantity.getDomProperty("value"))
        );
    }
}
