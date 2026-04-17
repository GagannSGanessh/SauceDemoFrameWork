package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(className = "inventory_item_name")
    private WebElement itemName;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(ConfigReader.getProperty("explicitWait"))));
        PageFactory.initElements(driver, this);
    }

    public boolean isProductInCart(String expectedProductName) {
        wait.until(ExpectedConditions.visibilityOf(itemName));
        return itemName.getText().equalsIgnoreCase(expectedProductName);
    }

    public CheckoutPage proceedToCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        return new CheckoutPage(driver);
    }
}
