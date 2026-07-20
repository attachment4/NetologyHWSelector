import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderCardTest {

    private WebDriver driver;

        @BeforeAll
        static void setUpAll() {
            WebDriverManager.chromedriver().setup();
        }
        @BeforeEach
        void setUp() {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            driver = new ChromeDriver(options);
            driver.get("http://localhost:9999");
        }
        @Test
        void shouldSubmitFormSuccessfully() {
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Иванов");
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71234567890");
            driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
            driver.findElement(By.cssSelector("button[type='button'][role='button']")).click();
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("[data-test-id=order-success]")));
            String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
            assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text);
        }
        @AfterEach
                void tearDown(){
        driver.quit();
    }
}