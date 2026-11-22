package ua.kpi.tzyapz.shopline.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@Tag("ui")
class CatalogTest {

    private static final String BASE_URL = "https://practicesoftwaretesting.com/";
    private static final By PRODUCT_NAMES = By.cssSelector("[data-test='product-name']");
    private static final By HAND_TOOLS = By.xpath("//label[contains(normalize-space(.), 'Hand Tools')]");

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1280,900");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.ignoring(StaleElementReferenceException.class);
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_NAMES));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("TC-009 / F-50: фільтрація каталогу за категорією Hand Tools")
    void filterProductsByHandToolsCategory() {
        WebElement handTools = wait.until(ExpectedConditions.elementToBeClickable(HAND_TOOLS));
        handTools.click();

        wait.until(d -> handToolsCheckbox().isSelected());
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".spinner-border, [data-test='loading-spinner']")));
        List<String> results = wait.until(d -> {
            List<String> names = visibleProductNames();
            return names.isEmpty() ? null : names;
        });

        assertAll("Фільтрація Hand Tools",
                () -> assertTrue(handToolsCheckbox().isSelected(), "Категорія Hand Tools не вибрана"),
                () -> assertFalse(results.isEmpty(), "Після фільтрації немає товарів")
        );
    }

    @Test
    @DisplayName("TC-010 / F-60: пошук PliErs без урахування регістру")
    void searchProductsCaseInsensitively() {
        WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test='search-query']")));
        search.clear();
        search.sendKeys("PliErs");
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-test='search-submit']"))).click();

        wait.until(d -> d.findElement(By.tagName("body")).getText().contains("Searched for: PliErs"));
        List<String> results = wait.until(d -> {
            List<String> names = visibleProductNames();
            return !names.isEmpty()
                    && names.stream().allMatch(name -> name.toLowerCase(Locale.ROOT).contains("pliers"))
                    ? names : null;
        });

        assertAll("Пошук PliErs",
                () -> assertFalse(results.isEmpty(), "Пошук не повернув товарів"),
                () -> assertTrue(results.stream().allMatch(
                        name -> name.toLowerCase(Locale.ROOT).contains("pliers")),
                        "У результатах є товар без pliers у назві")
        );
    }

    private WebElement handToolsCheckbox() {
        WebElement label = driver.findElement(HAND_TOOLS);
        String id = label.getAttribute("for");
        if (id != null && !id.isBlank()) {
            return driver.findElement(By.id(id));
        }
        return label.findElement(By.xpath(
                ".//input[@type='checkbox'] | ./preceding-sibling::input[@type='checkbox']"));
    }

    private List<String> visibleProductNames() {
        return driver.findElements(PRODUCT_NAMES).stream()
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .toList();
    }
}
