package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddtoCartPage {

    private final WebDriver driver;

    private final By addToCartButton = By.id("add-to-cart-sauce-labs-backpack");
    private final By removeFromCartButton = By.id("remove-sauce-labs-backpack");
    private final By cartIcon = By.className("shopping_cart_container");

    public AddtoCartPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addItemToCart() {
        driver.findElement(addToCartButton).click();
    }

    public void removeItemFromCart() {
        driver.findElement(removeFromCartButton).click();
    }

    public boolean isItemAddedToCart() {
        return driver.findElement(removeFromCartButton).isDisplayed();
    }

    public String getCartItemCount() {
        return driver.findElement(cartIcon).getText();
    }
}