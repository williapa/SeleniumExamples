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

import org.junit.Assert;
import org.junit.Assume;
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
        qe = new DynamoDBQueryExpression();
        Assertion at = new Assertion();
        qe.withHashKeyValues(at);
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
        System.out.println("comparator: " + comparator);
        if ("=".equals(comparator.substring(0,1))) {
            return found == expected;
        } else if (">=".equals(comparator.substring(0, 2))) {
            return found >= expected;
        } else if ("<=".equals(comparator.substring(0, 2))) {
            return found <= expected;
        } else if (">".equals(comparator.substring(0,1))) {
            return found > expected;
        } else if ("<".equals(comparator.substring(0,1))) {
            return found < expected;
        } else {
            System.out.println("error comparing ints in compare");
            return false;
        }
    }

    public static boolean assertSize(WebDriver driver, Assertion assertion) {
        String expected = assertion.getExpected();
        int size = driver.findElements(getBy(assertion)).size();
        String value = expected.replaceFirst("(>)*(<)*(=)*", "");
        return compare(assertion.getExpected(), size, parseInt(value));
    }

    public static boolean assertStringLength(WebElement element, String expected) {
        String x = expected;
        String value = expected.replaceFirst("(>)*(<)*(=)*", "");
        return compare(x, element.getText().length(), parseInt(value));
    }

    public static boolean executeAssertion(WebDriver driver, WebElement element, By selector, Assertion assertion) {
        String attribute = assertion.getAttribute();
        String expected = assertion.getExpected();
        if (attribute.contains("foreach")) {
            //only compatible for text, length, visible, and attributes
            return forEachAssertion(driver, selector, assertion, attribute);
        } else if ("text".equals(attribute)) {
            return element.getText().matches(expected);
        } else if ("length".equals(attribute)) {
            return assertStringLength(element, expected);
        } else if ("size".equals(attribute)) {
           return assertSize(driver, assertion);
        } else if ("visible".equals(attribute)) {
            return element.isDisplayed();
        } else if("title".equals(attribute)) {
            return driver.getTitle().matches(expected);
        } else {
            return element.getAttribute(attribute).matches(expected);
        }
    }

    public static boolean forEachAssertion(WebDriver driver, By selector, Assertion assertion, String attribute) {
        String attr = attribute.replace("foreach ", "");
        List<WebElement> elements = driver.findElements(selector);
        List<Boolean> results = new ArrayList<>();
        for(WebElement element: elements) {
            results.add(executeAssertion(driver, element, selector, assertion));
        }
        return !results.contains(false);
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
        List<Assertion> relevantAssertions = assertions.stream()
            .filter(assertion -> testData.getAssertions().contains(assertion.getId()))
            .collect(Collectors.toList());
        for(Assertion assertion : relevantAssertions){
            System.out.println("executing assertion number: " + assertion.getId());
            By selector = getBy(assertion);
            WebElement element = driver.findElement(selector);
            boolean result = executeAssertion(driver, element, selector, assertion);
            if(!result) {
                System.out.println(testData.getMethodName() + "failed.");
                System.out.println("assertion: " + assertion.getDescription());
            } else {
                System.out.println("Assertion " + assertion.getId() + " passed.");
            }
            results.add(result);
        }
        driver.close();
        return !results.contains(false);
    }

    public static String methodName() { return Thread.currentThread().getStackTrace()[2].getMethodName(); }

    public void execute(TestData testData) throws Exception {
        Assume.assumeFalse(testData.skippable());
        boolean res = executeTest(driverFactory.getFirefoxDriver(), testData);
        Assert.assertEquals(res, true);
    }

    public static void takeScreenshot(WebDriver driver, String fileName) throws Exception {
        // Take screenshot and store as a file format
        File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        // now copy the  screenshot to desired location using copyFile //method
        FileUtils.copyFile(src, new File(System.getProperty("user.dir") + "/testScreenshots/" + fileName + ".png"));
        screenshotNumber++;
    }
}
