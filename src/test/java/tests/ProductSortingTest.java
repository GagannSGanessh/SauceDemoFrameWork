package tests;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductSortingTest extends BaseClass {

    @Test
    public void sortProductsNameZtoATest() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(ConfigReader.getProperty("standardUser"), ConfigReader.getProperty("password"));

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        
        // Get initial names and sort them conceptually
        List<String> originalNames = inventoryPage.getAllProductNames();
        List<String> expectedSortedNames = new ArrayList<>(originalNames);
        expectedSortedNames.sort(Collections.reverseOrder()); // Z to A

        // Perform sorting on the web application
        inventoryPage.sortBy("za");

        // Verify the application states matches the expected logic
        List<String> actualSortedNames = inventoryPage.getAllProductNames();
        Assert.assertEquals(actualSortedNames, expectedSortedNames, "Products are not sorted from Z to A properly.");
    }

    @Test
    public void sortProductsPriceLowToHighTest() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(ConfigReader.getProperty("standardUser"), ConfigReader.getProperty("password"));

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        
        // Get initial prices and sort them
        List<Double> originalPrices = inventoryPage.getAllProductPrices();
        List<Double> expectedSortedPrices = new ArrayList<>(originalPrices);
        Collections.sort(expectedSortedPrices); // Low to High

        // Sort on via UI
        inventoryPage.sortBy("lohi");

        // Verify
        List<Double> actualSortedPrices = inventoryPage.getAllProductPrices();
        Assert.assertEquals(actualSortedPrices, expectedSortedPrices, "Products are not sorted from Low to High properly.");
    }
}
