package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ApiTestListener implements ITestListener {

    private static ExtentReports extent = ApiExtentManager.getInstance();
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "API Test Passed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Here we embed the raw HTTP trace / error as an API "Screenshot"
        extentTest.get().log(Status.FAIL, MarkupHelper.createLabel("API Test Failed", ExtentColor.RED));
        extentTest.get().log(Status.FAIL, "<pre>" + result.getThrowable().getMessage() + "</pre>");
        
        StackTraceElement[] trace = result.getThrowable().getStackTrace();
        StringBuilder traceStr = new StringBuilder();
        for (int i=0; i < Math.min(10, trace.length); i++) {
            traceStr.append(trace[i].toString()).append("\n");
        }
        extentTest.get().log(Status.FAIL, "<details><summary>Click to view Stack Trace</summary><pre>" + traceStr.toString() + "</pre></details>");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, "API Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
}
