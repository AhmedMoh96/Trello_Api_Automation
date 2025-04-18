import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TestBase {
    protected static ExtentReports report;
    protected static ExtentTest test;  // Declare test instance

    @BeforeSuite
    public void setupReport() {
        report = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("Reports/report.html");
        spark.config().setTheme(Theme.DARK);
        spark.config().setReportName("Trello API Automation");
        spark.config().setDocumentTitle("API Testing Report");
        report.attachReporter(spark);
    }

    @AfterSuite
    public void tearDownReport() throws IOException {
        report.flush();
        Desktop.getDesktop().open(new File("Reports/report.html"));
    }
}
