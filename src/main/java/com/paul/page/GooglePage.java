package com.paul.page;

import com.paul.domain.Market;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GooglePage extends BasePage {

    private WebDriver driver;
    private static Market market;

    // Page URL
    private static String US_PAGE_URL="https://www.google.com";
    private static String UK_PAGE_URL="https://www.google.co.uk";
    private static String MX_PAGE_URL="https://www.google.com.mx";

    // Locators
    @FindBy(css="input[title='Search']")
    private WebElement searchInput;

    @FindBy(css="input[type='submit']")
    private WebElement searchButton;

    @FindBy(css="input[title='Buscar']")
    private WebElement MX_searchInput;

    public boolean validate() throws Exception {
        WebElement si = getElementByMarket("searchInput", this.market);
        System.out.println("is valid: " + si.isDisplayed() + " for marketplace: " + this.market);

        return si.isDisplayed() || driver.getTitle().equals("Robot Checker");
    }

    // Constructor
    public GooglePage(WebDriver driver) {
        this.driver = driver;
        driver.get(US_PAGE_URL);
        this.market = Market.US;
        // Initialise Element (can't be done in abstract bc no url)
        PageFactory.initElements(driver, this);
    }

    // marketplace constructor
    public GooglePage(WebDriver driver, String market) throws Exception {
        this.driver = driver;
        if (market == "UK") {
            driver.get(UK_PAGE_URL);
            this.market = Market.UK;
        } else if (market == "MX") {
            driver.get(MX_PAGE_URL);
            this.market = Market.MX;
        } else {
            driver.get(US_PAGE_URL);
            this.market = Market.US;
        }
        screenAndInit(driver, this.market, this);
    }

    public GooglePage(WebDriver driver, Market market) throws Exception {
        this.driver = driver;
        this.market = market;
        if (market == Market.UK){
            driver.get(UK_PAGE_URL);
        } else if (market == Market.MX){
            driver.get(MX_PAGE_URL);
        } else {
            driver.get(US_PAGE_URL);
        }
        screenAndInit(driver, this.market, this);
    }

    public void enterText(String string) {
        searchInput.sendKeys(string);
    }

    public GoogleResultsPage clickSearch() {
        searchButton.click();
        return new GoogleResultsPage(driver);
    }
}
