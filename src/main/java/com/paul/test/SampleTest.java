package com.paul.test;

import com.paul.driver.DriverFactory;
import com.paul.page.*;

import org.openqa.selenium.*;

import org.junit.Test;
import org.junit.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SampleTest extends BaseTest {

	private static String search = "Harry Potter and the Order Of The Phoenix Kindle";
	private static String expectedFirstResult = "https://www.amazon.com";

    private void googleHarryPotterSearch(WebDriver driver) throws Exception {
        GooglePage googleHome = new GooglePage(driver);
        googleHome.enterText(search);
        GoogleResultsPage results = googleHome.clickSearch();
        String link = results.getResult();
        Assert.assertEquals(link, expectedFirstResult);
        AmazonDetailPage adp = new AmazonDetailPage(driver);
        adp.customersAlsoBoughtRightButton.getText();
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
        List<String> markets = new ArrayList<String>();
        markets.add("UK");
        markets.add("US");
        markets.add("MX");
        for(String s: markets) {
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