import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SeleniumTrainingTest {

    private static final Logger logger = LogManager.getLogger(SeleniumTrainingTest.class);
    private static final String BASE_URL =  System.getenv("BASE_URL");;
    private WebDriver driver;

    @BeforeAll
    static void init() {
        logger.info("==========================================");
        logger.info("НАЧАЛО ВЫПОЛНЕНИЯ ТЕСТОВ");
        logger.info("Время начала: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        logger.info("URL тестовой страницы: {}", BASE_URL);
        WebDriverManager.chromedriver().setup();
        logger.info("WebDriverManager настроен, драйвер готов к использованию");
    }

    @BeforeEach
    void setUpTest() {
        logger.info("------------------------------------------");
        logger.info("Подготовка к выполнению нового теста");
    }

    @AfterEach
    void driverClose() {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Драйвер успешно закрыт");
            } catch (Exception e) {
                logger.error("Ошибка при закрытии драйвера: {}", e.getMessage());
            }
            driver = null;
        }
        logger.info("Завершение теста");
    }

    @AfterAll
    static void tearDownAll() {
        logger.info("==========================================");
        logger.info("ВЫПОЛНЕНИЕ ВСЕХ ТЕСТОВ ЗАВЕРШЕНО");
        logger.info("Время окончания: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        logger.info("==========================================");
    }

    @Test
    void test1_HeadlessTextInput() {
        logger.info("Запуск теста 1: Headless режим");
        logger.info("Настройка ChromeOptions для headless режима");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        logger.info("ChromeOptions созданы: {}", options.addArguments());

        driver = new ChromeDriver(options);
        logger.info("ChromeDriver создан в headless режиме");

        logger.info("Открытие страницы: {}", BASE_URL);
        driver.get(BASE_URL);
        logger.info("Страница успешно открыта, заголовок: {}", driver.getTitle());
        logger.info("Текущий URL: {}", driver.getCurrentUrl());

        WebElement inputField = driver.findElement(By.id("textInput"));
        logger.info("Найден элемент textInput с локатором By.id(\"textInput\")");

        String testText = "ОТУС";
        logger.info("Ввод текста в поле: '{}'", testText);
        inputField.sendKeys(testText);

        String value = inputField.getAttribute("value");
        logger.info("Получено значение из поля: '{}'", value);

        if ("ОТУС".equals(value)) {
            logger.info("✅ ТЕСТ 1 ПРОЙДЕН УСПЕШНО: текст совпадает");
        } else {
            logger.error("❌ ТЕСТ 1 НЕ ПРОЙДЕН: ожидалось 'ОТУС', получено '{}'", value);
            assert false : "Текст не совпадает. Было: " + value;
        }
    }

    @Test
    void test2_KioskModal() {
        logger.info("Запуск теста 2: Режим киоска");
        logger.info("Настройка ChromeOptions для режима киоска");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--kiosk");
        logger.info("ChromeOptions созданы: {}", options.getBrowserName());

        driver = new ChromeDriver(options);
        logger.info("ChromeDriver создан в режиме киоска");

        logger.info("Открытие страницы: {}", BASE_URL);
        driver.get(BASE_URL);
        logger.info("Страница успешно открыта, размер окна: {}", driver.manage().window().getSize());

        logger.info("Поиск кнопки открытия модального окна с локатором By.id(\"openModalBtn\")");
        WebElement modalButton = driver.findElement(By.id("openModalBtn"));
        logger.info("Кнопка найдена, текст кнопки: '{}'", modalButton.getText());

        logger.info("Нажатие на кнопку открытия модального окна");
        modalButton.click();
        logger.info("Кнопка нажата");

        logger.info("Ожидание появления модального окна (таймаут 5 секунд)");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("myModal")));
        logger.info("Модальное окно найдено с локатором By.id(\"myModal\")");

        boolean isDisplayed = modal.isDisplayed();
        logger.info("Модальное окно отображается: {}", isDisplayed);

        if (isDisplayed) {
            logger.info("✅ ТЕСТ 2 ПРОЙДЕН УСПЕШНО: модальное окно открылось и отображается");
        } else {
            logger.error("❌ ТЕСТ 2 НЕ ПРОЙДЕН: модальное окно не отображается");
            assert false : "Модальное окно не открылось";
        }
    }

    @Test
    void test3_FullscreenForm() {
        logger.info("Запуск теста 3: Полноэкранный режим");
        logger.info("Настройка ChromeOptions для полноэкранного режима");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");

        driver = new ChromeDriver(options);
        logger.info("ChromeDriver создан в полноэкранном режиме");

        logger.info("Открытие страницы: {}", BASE_URL);
        driver.get(BASE_URL);
        logger.info("Страница успешно открыта");

        logger.info("Заполнение формы:");
        logger.info("Поиск поля имени с локатором By.id(\"name\")");
        WebElement nameInput = driver.findElement(By.id("name"));
        String testName = "фыв";
        logger.info("Ввод имени: '{}'", testName);
        nameInput.sendKeys(testName);
        logger.info("Имя введено, значение поля: '{}'", nameInput.getAttribute("value"));

        logger.info("Поиск поля email с локатором By.id(\"email\")");
        WebElement emailInput = driver.findElement(By.id("email"));
        String testEmail = "asdf@sdfg.rt";
        logger.info("Ввод email: '{}'", testEmail);
        emailInput.sendKeys(testEmail);
        logger.info("Email введён, значение поля: '{}'", emailInput.getAttribute("value"));

        logger.info("Поиск кнопки отправки формы с локатором By.cssSelector(\"#sampleForm button[type='submit']\")");
        WebElement submitButton = driver.findElement(By.cssSelector("#sampleForm button[type='submit']"));
        logger.info("Текст кнопки: '{}'", submitButton.getText());

        logger.info("Нажатие на кнопку отправки формы");
        submitButton.click();
        logger.info("Форма отправлена");

        logger.info("Ожидание изменения сообщения (таймаут 5 секунд)");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        logger.info("Начало ожидания изменения текста в элементе messageBox");

        WebElement message = wait.until(driver -> {
            WebElement el = driver.findElement(By.id("messageBox"));
            String currentText = el.getText();
            logger.debug("Текущий текст сообщения: '{}'", currentText);
            return !currentText.equals("Это динамическое сообщение.") ? el : null;
        });

        logger.info("Ожидание завершено, сообщение изменилось");
        logger.info("Найден элемент messageBox с локатором By.id(\"messageBox\")");

        String actualText = message.getText();
        logger.info("Полный текст сообщения: '{}'", actualText);

        boolean containsName = actualText.contains(testName);
        boolean containsEmail = actualText.contains(testEmail);
        logger.info("Сообщение содержит имя '{}': {}", testName, containsName);
        logger.info("Сообщение содержит email '{}': {}", testEmail, containsEmail);

        if (containsName && containsEmail) {
            logger.info("✅ ТЕСТ 3 ПРОЙДЕН УСПЕШНО: сообщение содержит все ожидаемые данные");
        } else {
            logger.error("❌ ТЕСТ 3 НЕ ПРОЙДЕН: ожидалось сообщение с именем '{}' и email '{}', получено: '{}'",
                    testName, testEmail, actualText);
            assert false : "Неправильное сообщение: " + actualText;
        }
    }
}