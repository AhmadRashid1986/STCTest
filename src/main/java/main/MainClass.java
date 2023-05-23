package main;

import org.openqa.selenium.chrome.ChromeOptions;
import wizard.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainClass extends Thread {

    public static WebDriver driver;
    public static String imageWizardPath = System.getProperty("user.dir") + "\\STC.jpg";
    public static String driverPath = System.getProperty("user.dir");
    public static String browserType;
    public static String[] browserSelection = {"Chrome", "FireFox", "IE"};
    public static String reportPath = System.getProperty("user.dir") + "\\Reports\\";
    public String screenShotLocationXMLPath = System.getProperty("user.dir") + "\\ScreenShots\\";
    public static String mainWindow;
    Node node = null;
    public static Thread wizardThread = new Thread(new MainClass());
    public static Thread currentThread = Thread.currentThread();
    public static boolean wizardFinished = false;
    public static ExtentReports extent;
    public static ExtentTest logger;
    public static String extentReportPath;
    public static String elementName;
    static ExtentSparkReporter report;
    public static String mainElementsPageXMLFile = System.getProperty("user.dir") + "\\src\\main\\resources\\MainPageElements.xml";
    public static String elementsKSAPageXMLFile = System.getProperty("user.dir") + "\\src\\main\\resources\\KSAPageElements.xml";
    public static String elementsBAHPageXMLFile = System.getProperty("user.dir") + "\\src\\main\\resources\\BahrainPageElements.xml";
    public static String elementsKUWPageXMLFile = System.getProperty("user.dir") + "\\src\\main\\resources\\KuwaitPageElements.xml";
    public static String url = "https://subscribe.stctv.com/sa-en";
    public static String[] countryList = {"Bahrain","KSA","Kuwait"};
    public static String selectedCountry;

    @BeforeClass
    public static void setupCreation() throws Exception {

        inputDialog();
        Logger.getRootLogger().setLevel(Level.OFF);

        driverSettings(url);
        initializeReport();

    }

    public String readLocator(String XMLFile, String element) {

        String elementValue = "";
        File Resource = null;
        elementName = element;

        // Check if the File exists and has values
        try {
            Resource = new File(XMLFile); // Define file path to object from File type
            //Check if resource exists and is a file not a directory to start processing the file data
            org.junit.Assert.assertTrue("XMLFile location isn't exist --> " + XMLFile, Resource.exists() && !Resource.isDirectory());
            //Check if the file has data or isEmpty
            org.junit.Assert.assertTrue("XMLFile hasn't data --> " + XMLFile, Resource.length() != 0);
        } catch (Throwable throwable) {
            throwable.printStackTrace(System.out);
            org.junit.Assert.fail("Something went wrong, check the log");
        }

        try {
            //Read provided file and check element exist
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //Parsing the File data as document
            Document document = documentBuilder.parse(Resource);
            //Formatting File data to start processing
            document.getDocumentElement().normalize();

            // Read provided tag from XML and return it's node list object
            NodeList nodeList = document.getElementsByTagName(element);
            // Check if the tag element exist or not
            org.junit.Assert.assertFalse("The tag element isn't exist --> " + element + " in " + XMLFile, nodeList.getLength() == 0);

            // Get element value and save in element value variable
            elementValue = nodeList.item(0).getTextContent();
        } catch (Throwable throwable) {
            throwable.printStackTrace(System.out);
            org.junit.Assert.fail("Something went wrong, check the log for --> " + XMLFile);
        }
        //Check if the element is null value
        org.junit.Assert.assertFalse("The element hasn't value --> " + element + " in " + XMLFile, elementValue == null);
        //Check the element isEmpty
        org.junit.Assert.assertFalse("The element hasn't value --> " + element + " in " + XMLFile, elementValue.isEmpty());
        return elementValue;
    }

    @Rule
    public TestRule watchman = new TestWatcher() {

        @Override
        public Statement apply(Statement base, Description description) {
            return super.apply(base, description);
        }

        @Override
        protected void starting(Description description) {
            super.starting(description);
            report.config().setReportName(description.getClassName());

            logger = extent.createTest(description.getMethodName(), description.getClassName());
            logger.log(Status.INFO, "Starting the tests ");

        }

        @Override
        protected void succeeded(Description description) {
            try {

                logger.pass(MarkupHelper.createLabel(description.getMethodName() + " PASS", ExtentColor.GREEN));
                logger.assignCategory("Passed Test Cases");
                logger.log(Status.INFO, "Starting the tests ");


            } catch (Exception e1) {

                System.out.println(e1.getMessage());

            }
        }

        @Override
        protected void failed(Throwable e, Description description) {
            try {

                logger.log(Status.FAIL, MarkupHelper.createLabel(e.getClass().getSimpleName() + ": " + elementName, ExtentColor.RED));
                logger.fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(screenShotOnFailure(e, description)).build());
                logger.assignCategory("Failed Test Cases");
                logger.log(Status.INFO, "Starting the test ");

            } catch (Exception e2) {
                System.out.println(e2.getMessage());

            }
        }

    };

    public String screenShotOnFailure(Throwable e, Description description) {

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH-mm-ss");
        Date date = new Date();

        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {

            String pathSNP = screenShotLocationXMLPath;
            File dir = new File(pathSNP);
            dir.getParentFile().mkdirs();
            String path = pathSNP + description.getDisplayName() + " "
                    + e.getClass().getSimpleName() + dateFormat.format(date) + ".png";

            FileUtils.copyFile(file, new File(path));

            driver.navigate().to(url);

            return path;

        } catch (IOException e2) {

            e.printStackTrace();
            System.out.print("failed");

        }

        return null;

    }

    @AfterClass
    public static void closeTest() throws IOException {

        driver.switchTo().window(mainWindow).close();
        driver.quit();

        extent.flush();

        File file = new File(extentReportPath);
        Desktop desktop = Desktop.getDesktop();
        desktop.open(file);

    }

    public List readTags(String elementTag, String elementsPath) throws Exception {

        String xmlFile = elementsPath;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        Element root = doc.getDocumentElement();
        List<String> list2 = new ArrayList<>();
        int n = 0;

        NodeList list = root.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                if (node.getNodeName().startsWith(elementTag)) {

                    list2.add(n, node.getNodeName());
                    n++;

                }

            }

        }

        return list2;

    }

    public static void inputDialog() throws Exception {

        wizardThread = new Thread(new Runnable() {
            @Override
            public void run() {

                Page1 page1OBJ = new Page1();
                page1OBJ.page1View();

            }
        });

        wizardThread.start();

        while (wizardFinished == false) {

            currentThread.join(10);

        }

    }

    public static WebDriver driverSettings(String url) {

        if (driver == null) {

            if (browserType == "Chrome") {//For chrome driver call
                System.setProperty("webdriver.http.factory", "jdk-http-client");
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                System.setProperty("webdriver.chrome.driver", driverPath + "\\chromedriver.exe");
                driver = new ChromeDriver();
            }

            if (browserType == "FireFox") {//For firefox driver call
                System.setProperty("webdriver.gecko.driver", driverPath + "\\geckodriver.exe");
                driver = new FirefoxDriver();
            }

            if (browserType == "Edge") {
                System.setProperty("webdriver.edge.driver", driverPath + "\\msedgedriver.exe");
                driver = new EdgeDriver();
            }

            mainWindow = driver.getWindowHandle();

            driver.navigate().to(url); //Navigate to the entered url in the top
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().window().maximize(); //Enlarge the browser page

        } else {

            driver.navigate().to(url); //Navigate to the entered url in the top

        }
        return driver;
    }

    public static void initializeReport() {

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH-mm-ss");

        Date date = new Date();

        extentReportPath = reportPath + "STCPlanTesting" + dateFormat.format(date) + ".html";
        report = new ExtentSparkReporter(extentReportPath);


        extent = new ExtentReports();

        report.config().setDocumentTitle("Automation Report");

        extent.attachReporter(report);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Browser ", browserType);

        report.config().setTheme(Theme.STANDARD);
        report.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm ");

    }

}