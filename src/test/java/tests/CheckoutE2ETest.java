package tests;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;

public class CheckoutE2ETest extends BaseClass {

    @Test
    public void e2eCheckoutFlowTest() {
        // 1. Login
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(ConfigReader.getProperty("standardUser"), ConfigReader.getProperty("password"));

        // 2. Inventory Page - add product
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        String targetProduct = "Sauce Labs Backpack";
        inventoryPage.addProductToCart(targetProduct);
        CartPage cartPage = inventoryPage.goToCart();

        // 3. Cart Page - verify and proceed
        Assert.assertTrue(cartPage.isProductInCart(targetProduct), "Target product not found in the cart.");
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();

        // 4. Checkout details
        checkoutPage.enterDetails("John", "Doe", "12345");
        
        // 5. Checkout finish
        checkoutPage.finishCheckout();

        // 6. Verification
        String confirmationMessage = checkoutPage.getConfirmationMessage();
        Assert.assertEquals(confirmationMessage.toLowerCase(), "thank you for your order!", "Checkout did not complete successfully.");
    }
}
