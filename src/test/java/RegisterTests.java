
import client.RestClient;
import client.UserRequest;
import pageobject.RegisterPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class RegisterTests {
    private UserRequest userClient;
    private WebDriver driver;
    private RegisterPage registerPage;

    User user = new User();

    @Before
    public void testCreateOrder() {
        userClient = new UserRequest();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get(RestClient.BASE_URL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        registerPage = new RegisterPage(driver);
    }
    @Test
    @DisplayName("Успешная Регистрация")
    public void successfullyRegistration() {
        registerPage.registration(
                user.getName(),
                user.getEmail(),
                user.getPassword());
        assertTrue(registerPage.enterButtonIsDisplayed());
    }
    @After
    public void tearDown() {
        ValidatableResponse response = userClient.login(user);
        String accessToken = response.extract().path("accessToken");
        userClient.delete(accessToken);

        driver.quit();
    }
}