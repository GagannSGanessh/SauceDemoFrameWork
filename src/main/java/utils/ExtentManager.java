package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/ExtentReport.html");
            sparkReporter.config().setDocumentTitle("Automation Execution Report");
            sparkReporter.config().setReportName("SauceDemo UI Tests");
            sparkReporter.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Application", "SauceDemo");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Executed By", "Gagan sg");
        }
        return extent;
    }
}
