package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> itemPrices;

    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(ConfigReader.getProperty("explicitWait"))));
        PageFactory.initElements(driver, this);
    }

    public boolean isLoaded() {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        return pageTitle.getText().equalsIgnoreCase("Products");
    }

    public void addProductToCart(String productName) {
        // Build locator dynamically or just iterate over and find the corresponding add button
        String xpath = String.format("//div[text()='%s']/../../..//button[text()='Add to cart']", productName);
        WebElement addButton = driver.findElement(org.openqa.selenium.By.xpath(xpath));
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
    }

    public CartPage goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
        return new CartPage(driver);
    }

    public void sortBy(String value) {
        wait.until(ExpectedConditions.visibilityOf(sortDropdown));
        Select select = new Select(sortDropdown);
        select.selectByValue(value); // values: az, za, lohi, hilo
    }

    public List<String> getAllProductNames() {
        return itemNames.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public List<Double> getAllProductPrices() {
        return itemPrices.stream()
                .map(el -> Double.parseDouble(el.getText().replace("$", "")))
                .collect(Collectors.toList());
    }
}
