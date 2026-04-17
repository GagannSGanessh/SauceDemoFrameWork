package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ApiExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/ApiExtentReport.html");
            sparkReporter.config().setDocumentTitle("API Execution Report");
            sparkReporter.config().setReportName("ReqRes API Automation");
            sparkReporter.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Application", "ReqRes API");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Executed By", "API Framework Extent Manager");
        }
        return extent;
    }
}
