package com.paul.test;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.paul.domain.Assertion;
import com.paul.domain.TestData;
import com.paul.driver.DriverFactory;

import org.apache.commons.io.FileUtils;

import org.junit.BeforeClass;

import org.openqa.selenium.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public abstract class BaseTest {

    private static String dir = System.getProperty("user.dir");
    private static String geckoDriver = "webdriver.gecko.driver";
    private static String geckoDriverPath = dir + "/geckodriver.exe";
    private static String phantomDriver = "phantomjs.binary.path";
    private static String phantomDriverPath = dir + "/phantomjs.exe";
    private static String chromeDriver = "webdiver.chrome.driver";
    private static String chromeDriverPath = dir + "/chromedriver.exe";
    protected static int screenshotNumber = 0;
    protected DriverFactory driverFactory = new DriverFactory();
    protected static List<TestData> testData;
    protected static List<Assertion> assertions;
    protected static DynamoDBMapper mapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_WEST_2)
            .build());

    @BeforeClass
    public static void config() {
        System.setProperty(geckoDriver, geckoDriverPath);
        System.setProperty(phantomDriver, phantomDriverPath);
        System.setProperty(chromeDriver, chromeDriverPath);
        // Query Test Data table
        DynamoDBQueryExpression qe = new DynamoDBQueryExpression();
        TestData td = new TestData();
        qe.withHashKeyValues(td);
        testData = mapper.scan(TestData.class, new DynamoDBScanExpression());
        // Now get Assertion Data
        for(TestData t: testData) {
            System.out.println(t.getAssertions());
            System.out.println(t.getMethodName());
            System.out.println(t.getUrl());
            System.out.println(t.isPassedSinceLastPush());
        }
        qe = new DynamoDBQueryExpression();
        Assertion a = new Assertion();
        qe.withHashKeyValues(a);
        assertions = mapper.scan(Assertion.class, new DynamoDBScanExpression());
    }

    public static By getBy(Assertion assertion) {
        String selector = assertion.getSelector();
        if (selector.matches("/^xpath=(.*)$/")) {
            return By.xpath(selector.substring(6));
        } else if (selector.matches("/^css=(.*)$/")) {
            return By.cssSelector(selector.substring(4));
        }
        // default: css without prefix
        return By.cssSelector(selector);
    }

    public static boolean compare(String comparator, int found, int expected) {
        if ("=".equals(comparator.charAt(0))) {
            return found == expected;
        } else if (">=".equals(comparator.substring(0, 2))) {
            return found >= expected;
        } else if ("<=".equals(comparator.substring(0, 2))) {
            return found <= expected;
        } else if (">".equals(comparator.charAt(0))) {
            return found > expected;
        } else if ("<".equals(comparator.charAt(0))) {
            return found < expected;
        } else {
            //throw exception;
            return false;
        }
    }

    public static boolean assertSize(WebDriver driver, Assertion assertion) {
        String expected = assertion.getExpected();
        int size = driver.findElements(getBy(assertion)).size();
        String value = expected.replaceFirst("/[><=]/", "");
        return compare(expected, size, parseInt(value));
    }

    public static boolean assertStringLength(WebElement element, String expected) {
        String value = expected.replaceFirst("/[><=]/", "");
        return compare(expected,element.getText().length(), parseInt(value));
    }

    public static boolean executeAssertion(WebDriver driver, WebElement element, Assertion assertion) {
        String attribute = assertion.getAttribute();
        String expected = assertion.getExpected();
        if ("text" == attribute) {
            return element.getText().matches(expected);
        } else if ("length" == attribute) {
            return assertStringLength(element, expected);
        } else if ("size" == attribute) {
           return assertSize(driver, assertion);
        } else if ("visible" == attribute) {
            return element.isDisplayed();
        } else if("title" == attribute) {
            return driver.getTitle().matches(expected);
        } else {
            return element.getAttribute(attribute).matches(expected);
        }
    }

    public TestData getTestDataByMethodName(String methodName) {
        List<TestData> result = testData.stream()
                .filter(testData -> methodName.equals(testData.getMethodName()))
                .collect(Collectors.toList());
        if(result.size() < 1) {
            System.out.println("ERROR: couldn't find testData by method name: " + methodName);
            return null;
        } else {
            return result.get(0);
        }
    }

    public boolean executeTest(WebDriver driver, TestData testData) throws Exception {
        driver.get(testData.getUrl());
        takeScreenshot(driver, testData.getMethodName());
        List<Boolean> results = new ArrayList<>();
        testData.getAssertions();
        List<Assertion> relevantAssertions = assertions.stream()
            .filter(assertion -> testData.getAssertions().contains(assertion.getId()))
            .collect(Collectors.toList());
        for(Assertion assertion : relevantAssertions){
            By selector = getBy(assertion);
            WebElement element = driver.findElement(selector);
            boolean result = executeAssertion(driver, element, assertion);
            if(!result) {
                System.out.println(testData.getMethodName() + "failed.");
                System.out.println("assertion: " + assertion.getDescription());
                System.out.println("url: " + testData.getUrl());
                System.out.println("selector: " + assertion.getSelector());
                System.out.println("attribute: " + assertion.getAttribute());
                System.out.println("expected: " + assertion.getExpected());
            }
            results.add(result);
        }
        driver.close();
        return !results.contains(false);
    }

    public static void takeScreenshot(WebDriver driver, String fileName) throws Exception {
        // Take screenshot and store as a file format
        File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        // now copy the  screenshot to desired location using copyFile //method
        FileUtils.copyFile(src, new File(System.getProperty("user.dir") + "/testScreenshots/" + fileName + ".png"));
        screenshotNumber++;
    }
}
