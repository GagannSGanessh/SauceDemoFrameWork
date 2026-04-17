package tests;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;

public class LoginTest extends BaseClass {

    @Test
    public void validLoginTest() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(ConfigReader.getProperty("standardUser"), ConfigReader.getProperty("password"));

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page was not loaded successfully after valid login.");
    }

    @Test
    public void lockedOutUserLoginTest() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(ConfigReader.getProperty("lockedOutUser"), ConfigReader.getProperty("password"));

        String actualMessage = loginPage.getErrorMessage();
        Assert.assertTrue(actualMessage.contains("Sorry, this user has been locked out"), "Expected locked out error message not displayed.");
    }

    @Test
    public void captureErrorScreenshotTest() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("invalid_user", "invalid_password");

        // The delay here is NOT static. Under the hood in LoginPage.java, getErrorMessage() uses dynamic 
        // explicit wait (WebDriverWait) to actively poll the DOM and continue the instant the error becomes visible.
        String actualError = loginPage.getErrorMessage();
        
        // We now force a failure by asserting the wrong string. The screen is already frozen with the UI error natively shown.
        Assert.assertEquals(actualError, "I expect this to be wrong", "Deliberately failing to capture the red UI error in a screenshot.");
    }
}
