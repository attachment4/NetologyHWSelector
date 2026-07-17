import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderCardTest {

    @Test
    void shouldSubmitFormSuccessfully() {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button[type='button'][role='button']")).click();
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test-id=order-success]")));
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        driver.quit();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text);

    }
}