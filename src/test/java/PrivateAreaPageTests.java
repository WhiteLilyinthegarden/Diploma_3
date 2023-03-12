import client.RestClient;
import client.UserRequest;
import pageobject.LoginPageBurger;
import pageobject.PrivateAreaPage;
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

public class PrivateAreaPageTests {
    private UserRequest userClient;
    private WebDriver driver;
    private PrivateAreaPage privateAreaPage;
    private LoginPageBurger objAuthorizationPage;

    User user = new User();

    @Before
    public void testCreateOrder() {
        userClient = new UserRequest();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get(RestClient.BASE_URL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        objAuthorizationPage = new LoginPageBurger(driver);
        privateAreaPage = new PrivateAreaPage(driver);

        userClient.create(user);
        objAuthorizationPage.loginByPrivateArea(user.getEmail(),
                user.getPassword());
    }
    @Test
    @DisplayName("Вход в 'Личный кабинет' с описанием")
    public void transitionInPrivateArea() {
        privateAreaPage.clickPrivateAreaButton();
        assertTrue(privateAreaPage.privateAreaDescriptionIsDisplayed());
    }
    @Test
    @DisplayName("Переход из личного кабинета в конструктор")
    public void transitionInBurgerConstructor() {
        privateAreaPage.clickPrivateAreaButton();
        privateAreaPage.clickConstructor();
        assertTrue(objAuthorizationPage.orderSubmitButtonIsDisplayed());
    }
    @Test
    @DisplayName("Переход на главную страницу по нажатию на лого")
    public void transitionInLogo() {
        privateAreaPage.clickPrivateAreaButton();
        privateAreaPage.clickLogo();
        assertTrue(objAuthorizationPage.orderSubmitButtonIsDisplayed());
    }
    @Test
    @DisplayName("выход по кнопке 'Выйти' в личном кабинете")
    public void transitionToLoginPageFromPrivateAreaPage() {
        privateAreaPage.logoutFromPrivateArea();
        assertTrue(privateAreaPage.loginTextIsDisplayed());
    }
    @After
    public void tearDown() {
        ValidatableResponse response = userClient.login(user);
        String accessToken = response.extract().path("accessToken");
        userClient.delete(accessToken);
        driver.quit();
    }
}