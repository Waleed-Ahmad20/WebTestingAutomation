package stepdefinitions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import pages.AddtoCartPage;
import pages.LoginPage;

public class AddtoCartStepDefinitions {

    private WebDriver driver;
    private AddtoCartPage addToCartPage;
    private LoginPage loginPage;
    private WebDriverWait wait;

    @Step("the user is logged in and on the products page")
    @Given("the user is logged in and on the products page")
    public void userIsLoggedInAndOnProductsPage() {
        System.setProperty("webdriver.edge.driver", "D:\\edgedriver\\msedgedriver.exe");
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new EdgeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60)); 

        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLoginButton();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("react-burger-menu-btn")));
        assertTrue(loginPage.isOnProductsPage());

        addToCartPage = new AddtoCartPage(driver);
    }

    @Step("the user adds the Sauce Labs Backpack to the cart")
    @When("the user adds the Sauce Labs Backpack to the cart")
    public void userAddsProductToCart() throws InterruptedException {
        Thread.sleep(1000);
        addToCartPage.addItemToCart();
    }

    @Step("the product should be successfully added to the cart")
    @Then("the product should be successfully added to the cart")
    public void productShouldBeSuccessfullyAddedToCart() throws InterruptedException {
        Thread.sleep(1000);
        assertTrue(addToCartPage.isItemAddedToCart());
    }

    @Step("the user removes the Sauce Labs Backpack from the cart")
    @Then("the cart item count should be {string}")
    public void cartItemCountShouldBe(String expectedCount) throws InterruptedException {
        Thread.sleep(1000);
        String actualCount = addToCartPage.getCartItemCount();
        assertEquals(expectedCount, actualCount);
        driver.quit();
    }
}