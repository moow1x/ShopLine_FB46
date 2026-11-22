package ua.kpi.tzyapz.shopline.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Tag("ui")
public abstract class BaseUiTest {

    protected static final String BASE_URL = "https://practicesoftwaretesting.com/";
    protected static final String CUSTOMER_EMAIL = "customer@practicesoftwaretesting.com";
    protected static final String CUSTOMER_PASSWORD = "welcome01";

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void setUpBrowser() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1280,900");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
