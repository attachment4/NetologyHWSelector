import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderCardTest {

    private WebDriver driver;
    private WebDriverWait wait;

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
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    // Задание 1: happy path

    @Test
    void shouldSubmitFormSuccessfully() {
        setName("Иван Иванов");
        setPhone("+71234567890");
        checkAgreement();
        submit();
        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test-id=order-success]")));
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                message.getText().trim());
    }

    // Задание 2: валидация полей

    @Test
    void shouldShowErrorForEmptyName() {
        setPhone("+71234567890");
        checkAgreement();
        submit();
        assertEquals("Поле обязательно для заполнения", errorTextOf("name"));
    }

    @Test
    void shouldShowErrorForNameInLatin() {
        setName("Ivan Ivanov");
        setPhone("+71234567890");
        checkAgreement();
        submit();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                errorTextOf("name"));
    }

    @Test
    void shouldShowErrorForEmptyPhone() {
        setName("Иван Иванов");
        checkAgreement();
        submit();
        assertEquals("Поле обязательно для заполнения", errorTextOf("phone"));
    }

    @Test
    void shouldShowErrorForInvalidPhone() {
        setName("Иван Иванов");
        setPhone("+7123");
        checkAgreement();
        submit();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                errorTextOf("phone"));
    }

    @Test
    void shouldHighlightCheckboxWhenAgreementIsNotChecked() {
        setName("Иван Иванов");
        setPhone("+71234567890");
        submit();
        wait.until(ExpectedConditions.attributeContains(
                By.cssSelector("[data-test-id=agreement]"), "class", "input_invalid"));
    }

    @Test
    void shouldHighlightOnlyFirstInvalidField() {
        submit();
        // подсвечивается только первое незаполненное поле — имя, телефон остаётся без ошибки
        assertEquals("Поле обязательно для заполнения", errorTextOf("name"));
        assertTrue(driver.findElements(By.cssSelector("[data-test-id=phone].input_invalid")).isEmpty(),
                "Поле телефона не должно подсвечиваться, пока не заполнено имя");
    }

    private void setName(String name) {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys(name);
    }

    private void setPhone(String phone) {
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(phone);
    }

    private void checkAgreement() {
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
    }

    private void submit() {
        driver.findElement(By.cssSelector("button.button")).click();
    }

    private String errorTextOf(String fieldTestId) {
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test-id=" + fieldTestId + "].input_invalid .input__sub")));
        return error.getText().trim();
    }
}
