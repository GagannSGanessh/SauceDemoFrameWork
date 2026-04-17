package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;

import java.time.Duration;

public class BaseClass {

    // ThreadLocal to support parallel test execution safely
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeMethod
    public void setUp() {
        String browser = ConfigReader.getProperty("browser").toLowerCase();

        switch (browser) {
            case "firefox":
                driver.set(new FirefoxDriver());
                break;
            case "edge":
                driver.set(new EdgeDriver());
                break;
            case "chrome":
            default:
                // Selenium 4 automatically manages drivers via Selenium Manager
                driver.set(new ChromeDriver());
                break;
        }

        getDriver().manage().window().maximize();
        int implicitWait = Integer.parseInt(ConfigReader.getProperty("implicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        
        getDriver().get(ConfigReader.getProperty("url"));
    }

    public WebDriver getDriver() {
        return driver.get();
    }

    public static String getBase64Screenshot() {
        return ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.BASE64);
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }
}
