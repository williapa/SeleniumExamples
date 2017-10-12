package com.paul;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.*;

import org.junit.Test;
import org.junit.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SampleTest {
    private static String dir = System.getProperty("user.dir");
	private static String search = "Harry Potter and the Order Of The Phoenix Kindle";
	private static String expectedFirstResult = "https://www.amazon.com";
    private static String geckoDriver = "webdriver.gecko.driver";
    private static String geckoDriverPath = dir + "/geckodriver.exe";
    private static String phantomDriver = "phantomjs.binary.path";
    private static String phantomDriverPath = dir + "/phantomjs.exe";
    private static String chromeDriver = "webdiver.chrome.driver";
    private static String chromeDriverPath = dir + "/chromedriver.exe";
    private static int screenshotNumber = 0;
    private DriverFactory driverFactory = new DriverFactory();

    @BeforeClass
    public static void config() {
	    System.setProperty(geckoDriver, geckoDriverPath);
	    System.setProperty(phantomDriver, phantomDriverPath);
	    System.setProperty(chromeDriver, chromeDriverPath);
    }

    public static void takeScreenshot(WebDriver driver, String fileName) throws Exception {
        // Take screenshot and store as a file format
        File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        // now copy the  screenshot to desired location using copyFile //method
        FileUtils.copyFile(src, new File(System.getProperty("user.dir") + "/testScreenshots/" + fileName + ".png"));
        screenshotNumber++;
    }

    private void googleHarryPotterSearch(WebDriver driver) throws Exception {
        GooglePage googleHome = new GooglePage(driver);
        googleHome.enterText(search);
        GoogleResultsPage results = googleHome.clickSearch();
        String link = results.getResult();
        Assert.assertEquals(link, expectedFirstResult);
    }

    private void getPriceOfBitcoin(WebDriver driver) throws Exception {
        driver.get("https://www.coindesk.com/price");
        WebElement latestData = driver.findElement(By.cssSelector(".latest .data"));
        String bitcoinPrice = latestData.getText();
        System.out.println(">>> current bitcoin price (if my selector worked: )");
        System.out.println(">>> " + bitcoinPrice);
        bitcoinPrice = bitcoinPrice.replace(",", "");
        boolean greaterThan3K = (3000f < Float.parseFloat(bitcoinPrice.substring(1)));
        Assert.assertEquals(true, greaterThan3K);
    }

    public void yahooHarryPotterSearch(WebDriver driver) throws Exception {
        YahooPage yahooHome = new YahooPage(driver);
        yahooHome.enterText(search);
        YahooResultsPage yahooResults = yahooHome.clickSearch();
        String res = yahooResults.getResult();
        Assert.assertEquals(res, expectedFirstResult);
    }

    public void amazonProductTest(WebDriver driver) throws Exception {
        AmazonHomePage home = new AmazonHomePage(driver);
        takeScreenshot(driver, "screenshotNumber"+screenshotNumber);
        Assert.assertEquals(home.verify(), true);
        /*home.enterSearchText(search);

        AmazonSearchResultsPage searchResults = home.clickSearch();

        Assert.assertEquals(true,searchResults.verify());

        AmazonDetailPage details = searchResults.clickFirstResult();

        AmazonSignInPage signIn = details.buyWithoutBeingSignedIn();

        Assert.assertEquals(true, signIn.verify()); */
    }

    public void validateGoogleOnEveryMarketplace(WebDriver driver) throws Exception {
        for(String s: GooglePage.markets) {
            GooglePage gp = new GooglePage(driver, s);
            gp.validate();
        }
    }

    // 15 tests in 1
    @Test
    public void googleAllMarketplacesAndDriversTest() throws Exception {
        Class[] cArg = new Class[1];
        cArg[0] = Boolean.class;
        Method phantom = DriverFactory.class.getMethod("getPhantomJSDriver");
        Method htc = DriverFactory.class.getMethod("getHTCDriver", cArg);
        Method firefox = DriverFactory.class.getMethod("getFirefoxDriver");
        List<Method> methods = new ArrayList<Method>();
        methods.add(phantom);
        methods.add(htc);
        methods.add(firefox);

        for (Method m : methods) {
            WebDriver d;
            if(m.getName().equals("getHTCDriver")) {
                d = (WebDriver) m.invoke(driverFactory, true);
            } else {
                d = (WebDriver) m.invoke(driverFactory);
            }
            validateGoogleOnEveryMarketplace(d);
            d.close();
        }
    }

	@Test
	public void HTCViaFirefoxTest() throws Exception {
        WebDriver htcDriver = driverFactory.getHTCDriver(true);
        amazonProductTest(htcDriver);
	}

	@Test
    public void AndroidViaPhantomTest() throws Exception {
        WebDriver android4Driver = driverFactory.getAndroid4Driver(true);
        amazonProductTest(android4Driver);
    }

    @Test
    public void FirefoxTest() throws Exception {
        WebDriver firefox = driverFactory.getFirefoxDriver();
        amazonProductTest(firefox);
    }

    @Test
    public void PhantomTest() throws Exception {
        WebDriver phantom = driverFactory.getPhantomDriver();
        amazonProductTest(phantom);
    }

    @Test
    public void ChromeTest() throws Exception {
        WebDriver chrome = driverFactory.getChromeDriver();
        amazonProductTest(chrome);
    }

    @Test
    public void allDriversTest() throws Exception {
        int count = 0;
        for (Method driverBuilder : driverFactory.getClass().getMethods()) {
            System.out.println("method: " + driverBuilder.getName());
            System.out.println("is accessible: " + driverBuilder.isAccessible());
            if (driverBuilder.getReturnType().equals(WebDriver.class)){
                System.out.println("interesting...");
                count++;
                if (driverBuilder.getParameterTypes().length > 0) {
                    Object[] args = {true};
                    WebDriver driver = (WebDriver) driverBuilder.invoke(driverFactory, args);
                    amazonProductTest(driver);

                } else {
                    WebDriver driver = (WebDriver) driverBuilder.invoke(driverFactory);
                    amazonProductTest(driver);
                }
            }
        }
        Assert.assertEquals((count > 0), true);
    }
}