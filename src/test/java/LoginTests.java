import client.RestClient;
import client.UserRequest;
import pageobject.LoginPageBurger;
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
import static org.junit.Assert.*;

public class LoginTests {
    private UserRequest userRequest;
    private WebDriver driver;
    private LoginPageBurger loginPageBurger;

    User user = new User();

    @Before
    public void testCreateOrder() {
        userRequest = new UserRequest();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get(RestClient.BASE_URL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        loginPageBurger = new LoginPageBurger(driver);
        userRequest.create(user);
    }
    @Test
    @DisplayName("Авторизация через кнопку Личный Кабинет")
    public void loginByPrivateAreaButton() {
        loginPageBurger.loginByPrivateArea(
                user.getEmail(),
                user.getPassword());
        assertTrue(loginPageBurger.orderSubmitButtonIsDisplayed());
    }
    @Test
    @DisplayName("Авторизация через кнопку Войти в аккаунт")
    public void loginByEnterAccountButton() {
        loginPageBurger.loginByEnterAccountButton(
                user.getEmail(),
                user.getPassword());
        assertTrue(loginPageBurger.orderSubmitButtonIsDisplayed());
    }
    @Test
    @DisplayName("Авторизация через кнопку Войти на странице восстановления пароля")
    public void loginByUpdatePasswordPage() {
        loginPageBurger.loginByChangePassword(
                user.getEmail(),
                user.getPassword());
        assertTrue(loginPageBurger.orderSubmitButtonIsDisplayed());
    }
    @After
    public void tearDown() {
        ValidatableResponse response = userRequest.login(user);
        String accessToken = response.extract().path("accessToken");
        userRequest.delete(accessToken);
        driver.quit();
    }
}