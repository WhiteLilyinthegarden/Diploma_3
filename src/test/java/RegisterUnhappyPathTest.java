import client.RestClient;
import client.UserRequest;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.openqa.selenium.chrome.ChromeOptions;
import pageobject.RegisterPage;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class RegisterUnhappyPathTest {
    private WebDriver driver;
    private RegisterPage registerPage;
    private UserRequest userRequest;
    User user = new User();
    String passwordLess6Char = "P@ss";

    @Before
    public void testCreateOrder() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        userRequest = new UserRequest();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.get(RestClient.BASE_URL);
        registerPage = new RegisterPage(driver);
    }

    @Test
    @DisplayName("Неккоректный пароль при Регистрации меннее 6 символов")
    public void registrationFail() {
        user.setPassword(passwordLess6Char);
        registerPage.registrationFailed(
                user.getName(),
                user.getEmail(),
                user.getPassword());
        assertTrue(registerPage.getPasswordError());
    }

    @After
    public void tearDown() {
        try {
            ValidatableResponse response = userRequest.login(user);
            String accessToken = response.extract().path("accessToken");
            userRequest.delete(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.quit();
    }
}