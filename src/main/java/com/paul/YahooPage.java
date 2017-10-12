package com.paul;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class YahooPage {

    private WebDriver driver;

    // Page URL
    private static String PAGE_URL = "https://www.yahoo.com";

    // Locators
    @FindBy(css="#uh-search-box")
    private WebElement searchInput;

    @FindBy(css="#uh-search-button")
    private WebElement searchButton;

    // Constructor
    public YahooPage(WebDriver driver) {

        this.driver = driver;

        driver.get(PAGE_URL);

        // Initialise Elements
        PageFactory.initElements(driver, this);

    }

    public void enterText(String string) {

        // WebElement searchInput = driver.findElement(By.cssSelector(searchInputSelector));

        searchInput.sendKeys(string);

    }

    public YahooResultsPage clickSearch(){

        // WebElement searchButton = driver.findElement(By.cssSelector(searchButtonSelector));

        searchButton.click();

        return new YahooResultsPage(driver);

    }
}



