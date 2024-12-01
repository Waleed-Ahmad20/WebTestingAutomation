package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    
    private final WebDriver driver;
    
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By productsTitle = By.className("inventory_container");
    
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }
    
    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }
    
    public void clickLoginButton() {
        driver.findElement(loginButton).click();
    }
    
    public boolean isLoggedIn() {
        return driver.findElement(productsTitle).isDisplayed();
    }

    public boolean isOnProductsPage(){
        return driver.findElement(By.id("react-burger-menu-btn")).isDisplayed();
    }

}
