package stepdefinitions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import pages.LoginPage;
import utilities.DatabaseUtility; 

public class LoginStepDefinitions {

    private WebDriver driver;
    private LoginPage loginPage;
    private WebDriverWait wait;

    @Step("the user is on the login page")
    @Given("the user is on the login page")
    public void userIsOnLoginPage() {
        System.setProperty("webdriver.edge.driver", "D:\\edgedriver\\msedgedriver.exe");
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new EdgeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60)); 
    }

    @Step("the user enters {string} and {string}")
    @When("the user enters {string} and {string}")
    public void userEntersCredentials(String username, String password) throws InterruptedException {
        String[] credentials = DatabaseUtility.getTestData();
        if (credentials != null) {
            Thread.sleep(500);
            loginPage.enterUsername(credentials[0]);
            Thread.sleep(500);
            loginPage.enterPassword(credentials[1]);
    }
}

    @Step("clicks on the login button")
    @When("clicks on the login button")
    public void clicksOnLoginButton() throws InterruptedException {
        Thread.sleep(500);
        loginPage.clickLoginButton();
    }

    @Step("the user should be logged in successfully")
    @Then("the user should be logged in successfully")
    public void userShouldBeLoggedInSuccessfully() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.app_logo"))); 
        assertTrue(loginPage.isLoggedIn()); 
        driver.quit();
    }
}