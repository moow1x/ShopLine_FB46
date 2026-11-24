package ua.kpi.tzyapz.shopline.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

final class ProductPage {

    static final String NAME = "Combination Pliers";
    static final By TITLE = By.cssSelector("h1[data-test='product-name']");
    static final By QUANTITY = By.cssSelector("[data-test='quantity']");
    static final By UNIT_PRICE = By.cssSelector("[data-test='unit-price']");
    static final By ADD_TO_CART = By.cssSelector("[data-test='add-to-cart']");

    private static final By SEARCH_QUERY = By.cssSelector("[data-test='search-query']");
    private static final By SEARCH_SUBMIT = By.cssSelector("[data-test='search-submit']");
    private static final By PRODUCT_CARD = By.xpath(
            "//a[starts-with(@data-test, 'product-')]"
                    + "[.//*[@data-test='product-name' and normalize-space(.)=\"" + NAME + "\"]]");

    private ProductPage() {
    }

    static void open(WebDriver driver, WebDriverWait wait, String baseUrl) {
        driver.get(baseUrl);
        WebElement search = wait.until(elementToBeClickable(SEARCH_QUERY));
        search.sendKeys(NAME);
        wait.until(elementToBeClickable(SEARCH_SUBMIT)).click();
        wait.until(elementToBeClickable(PRODUCT_CARD)).click();
    }
}
