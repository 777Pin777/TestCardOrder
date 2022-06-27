import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CallBackTest {

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testOfTrueValidation() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Михаил Петров");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79642875597");
        driver.findElement(By.cssSelector(".checkbox__text")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        String expected = "Ваша заявка успешно отправлена!";
        assertEquals(actualText, expected);
    }


    @Test
    public void testNotTrueValidationOfEnglishName() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Mihail Petrov");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79642875597");
        driver.findElement(By.cssSelector("button")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id = 'name'].input_invalid .input__sub")).getText().trim();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(actualText, expected);
    }

    @Test
    public void testNotTrueValidationOfEmptyName() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79642875597");
        driver.findElement(By.cssSelector(".checkbox__text")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id = 'name'].input_invalid .input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";
        assertEquals(actualText, expected);
    }

    @Test
    public void testNotTrueValidationOfIncorrectPhone() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Алексей");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("96969696969696");
        driver.findElement(By.cssSelector(".checkbox__text")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id = 'phone'].input_invalid .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79215876025.";
        assertEquals(actualText, expected);
    }

    @Test
    public void testIfNotClickCheckbox() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Михаил Петров");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79642875597");
        driver.findElement(By.cssSelector("button")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id = 'agreement'].input_invalid .checkbox__text")).getText().trim();
        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных";
        assertEquals(actualText, expected);
    }
}
