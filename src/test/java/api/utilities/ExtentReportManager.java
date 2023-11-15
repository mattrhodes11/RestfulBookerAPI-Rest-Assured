package api.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager implements ITestListener 
{
	public ExtentReports extent;
	public ExtentSparkReporter sparkReporter;
	public ExtentTest test;
	
	String reportName;
	
	public void onStart(ITestContext testContext)
	{
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		reportName = "Test-Report" + timeStamp + ".html";
		
		sparkReporter = new ExtentSparkReporter(".\\reports\\" + reportName);
		sparkReporter.config().setDocumentTitle("Test Automation Report - Rest Assured");
		sparkReporter.config().setReportName("Restful Booker API");
		sparkReporter.config().setTheme(Theme.DARK);
		
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Application", "Restful Booker API");
		extent.setSystemInfo("Operating System", System.getProperty("os.name"));
	}
	
	public void onTestSuccess(ITestResult testResult)
	{
		test = extent.createTest(testResult.getName());
		test.assignCategory(testResult.getMethod().getGroups());
		test.createNode(testResult.getName());
		test.log(Status.PASS, "Test passed");
	}
	
	public void onTestFailure(ITestResult testResult)
	{
		test = extent.createTest(testResult.getName());
		test.assignCategory(testResult.getMethod().getGroups());
		test.createNode(testResult.getName());
		test.log(Status.FAIL, "Test failed");
		test.log(Status.FAIL, testResult.getThrowable().getMessage());
	}
	
	public void onTestSkipped(ITestResult testResult)
	{
		test = extent.createTest(testResult.getName());
		test.assignCategory(testResult.getMethod().getGroups());
		test.createNode(testResult.getName());
		test.log(Status.SKIP, "Test skipped");
		test.log(Status.SKIP, testResult.getThrowable().getMessage());
	}
	
	public void onFinish(ITestContext testContext)
	{
		extent.flush();
	}
}
