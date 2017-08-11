package com.paul;

import org.openqa.selenium.*;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import org.junit.Test;

import org.junit.*;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;

public class SampleTest {

	private static String search = "Harry Potter and the Order Of The Phoenix Kindle";
	private static String expectedFirstResult = "https://www.amazon.com";
    private static String geckoDriver = "webdriver.gecko.driver";
    private static String geckoDriverPath = "C:/Users/paul/workspace/selenium/geckodriver.exe";
    private static String phantomDriver = "phantomjs.binary.path";
    private static String phantomDriverPath = "C:/Users/paul/workspace/selenium/phantomjs.exe";

    @BeforeClass
    public static void config() {

	    System.setProperty(geckoDriver, geckoDriverPath);

	    System.setProperty(phantomDriver, phantomDriverPath);

    }

    private void googleHarryPotterSearch(WebDriver driver) throws Exception {

        GooglePage googleHome = new GooglePage(driver);

        googleHome.enterText(search);

        GoogleResultsPage results = googleHome.clickSearch();

        String link = results.getResult();

        Assert.assertEquals(link, expectedFirstResult);

        //driver.close();

    }

    private void getPriceOfBitcoin(WebDriver driver) throws Exception {

        JavascriptExecutor jse = (JavascriptExecutor) driver;

        driver.get("https://www.coindesk.com/price");

        WebElement latestData = driver.findElement(By.cssSelector(".latest .data"));

        String bitcoinPrice = latestData.getText();

        System.out.println(">>> current bitcoin price (if my selector worked: )");
        System.out.println(">>> " + bitcoinPrice);

        bitcoinPrice = bitcoinPrice.replace(",", "");

        boolean greaterThan3K = (3000f < Float.parseFloat(bitcoinPrice.substring(1)));

        Assert.assertEquals(true, greaterThan3K);

        driver.close();
    }

    public void yahooHarryPotterSearch(WebDriver driver) throws Exception {

        YahooPage yahooHome = new YahooPage(driver);

        yahooHome.enterText(search);

        YahooResultsPage yahooResults = yahooHome.clickSearch();

        String res = yahooResults.getResult();

        Assert.assertEquals(res, expectedFirstResult);

        //driver.close();

    }

    public void amazonProductTest(WebDriver driver) throws Exception {

        AmazonHomePage home = new AmazonHomePage(driver);

        home.enterSearchText(search);

        AmazonSearchResultsPage searchResults = home.clickSearch();

        Assert.assertEquals(true,searchResults.verify());

        AmazonDetailPage details = searchResults.clickFirstResult();

        AmazonSignInPage signIn = details.buyWithoutBeingSignedIn();

        Assert.assertEquals(true, signIn.verify());

    }

	@Test
	public void firefoxTest() throws Exception {

        WebDriver firefoxDriver = new FirefoxDriver();

        firefoxDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        //googleHarryPotterSearch(firefoxDriver);

        //yahooHarryPotterSearch(firefoxDriver);

        amazonProductTest(firefoxDriver);

	}

	@Test
    public void phantomTest() throws Exception {

        WebDriver phantomDriver = new PhantomJSDriver();

        phantomDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        Dimension bigger = new Dimension(1280, 1024);

        phantomDriver.manage().window().setSize(bigger);

        amazonProductTest(phantomDriver);

        //getPriceOfBitcoin(phantomDriver);

        //yahooHarryPotterSearch(phantomDriver);

    }

}