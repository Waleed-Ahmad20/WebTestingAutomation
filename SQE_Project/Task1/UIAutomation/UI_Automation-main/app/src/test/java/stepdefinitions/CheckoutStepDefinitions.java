package stepdefinitions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import pages.AddtoCartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import utilities.ExcelUtil;

public class CheckoutStepDefinitions {

    private WebDriver driver;
    private CheckoutPage checkoutPage;
    private AddtoCartPage addToCartPage;
    private LoginPage loginPage;
    private WebDriverWait wait;

    @Step("the user is logged in and on products page")
    @Given("the user is logged in and on products page")
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
        checkoutPage = new CheckoutPage(driver);
    }

    @Step("the user adds the Sauce Labs Backpack to cart")
    @When("the user adds the Sauce Labs Backpack to cart")
    public void userAddsProductToCart() throws InterruptedException {
        Thread.sleep(1000);
        addToCartPage.addItemToCart();
    }

    @Step("the user clicks on the shopping cart icon")
    @And("the user clicks on the shopping cart icon")
    public void userClicksOnCartIcon() throws InterruptedException {
        Thread.sleep(1000);
        checkoutPage.clickCartIcon();
    }

    @Step("the user clicks on the checkout button")
    @And("the user clicks on the checkout button")
    public void userClicksOnCheckoutButton() throws InterruptedException {
        Thread.sleep(1000);
        checkoutPage.clickCheckoutButton();
    }

    @Step("the user enters {string} and {string} and {string}") 
    @And("the user enters {string} and {string} and {string}")
    public void userEntersCheckoutDetails(String firstName, String lastName, String postalCode) throws InterruptedException {
        String[] data = ExcelUtil.getTestData(firstName, lastName, postalCode);
        if(data[0] != null){
        checkoutPage.enterFirstName(data[0]);
        }
        else{
            checkoutPage.enterFirstName(firstName);
        }
        Thread.sleep(1000);
        if(data[1] != null){
        checkoutPage.enterLastName(data[1]);
        }
        else{
            checkoutPage.enterLastName(lastName);
        }
        Thread.sleep(1000);
        if(data[2] != null){
        checkoutPage.enterPostalCode(data[2]);
        }
        else{
            checkoutPage.enterPostalCode(postalCode);
        }
    }

    @Step("the user clicks on the continue button")
    @And("the user clicks on the continue button")
    public void userClicksOnContinueButton() throws InterruptedException {
        Thread.sleep(1000);
        checkoutPage.clickContinueButton();
    }

    @Step("the user clicks on the finish button")
    @And("the user clicks on the finish button")
    public void userClicksOnFinishButton() throws InterruptedException {
        Thread.sleep(1000);
        checkoutPage.clickFinishButton();
    }

    @Step("the checkout process should be completed successfully")
    @Then("the checkout process should be completed successfully")
    public void checkoutProcessShouldBeCompletedSuccessfully() throws InterruptedException {
        Thread.sleep(1000);
        assertTrue(checkoutPage.isCheckoutComplete());
    }
}