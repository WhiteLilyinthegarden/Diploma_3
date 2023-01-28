import client.RestClient;
import client.UserRequest;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.response.ValidatableResponse;
import model.User;
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

    @Before
    public void testCreateOrder() {
        userRequest = new UserRequest();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get(RestClient.BASE_URL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        registerPage = new RegisterPage(driver);
        userRequest.create(user);
    }
    @Test
    @DisplayName("Неккоректный пароль при Регистрации меннее 6 символов")
    public void registrationFail() {
        registerPage.registrationFailed(
                user.getName(),
                user.getEmail(),
                "P@ss");
        assertTrue(registerPage.getPasswordError());
    }
    @After
    public void tearDown() {
        ValidatableResponse response = userRequest.login(user);
        String accessToken = response.extract().path("accessToken");
        userRequest.delete(accessToken);
        driver.quit();
    }
}