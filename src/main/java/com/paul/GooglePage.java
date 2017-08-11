package com.paul;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

import com.paul.GoogleResultsPage;

public class GooglePage {

    private WebDriver driver;

    // Page URL
    private static String PAGE_URL="https://www.google.com";

    // private static String searchInputSelector = "input[title='Search']";

    // private static String searchButtonSelector = "input[type='submit']";

    // Locators
    @FindBy(css="input[title='Search']")
    private WebElement searchInput;

    @FindBy(css="input[type='submit']")
    private WebElement searchButton;

    // Constructor
    public GooglePage(WebDriver driver) {

        this.driver = driver;

        driver.get(PAGE_URL);

        // Initialise Elements
        PageFactory.initElements(driver, this);

    }

    public void enterText(String string) {

        // WebElement searchInput = driver.findElement(By.cssSelector(searchInputSelector));

        searchInput.sendKeys(string);

    }

    public GoogleResultsPage clickSearch(){

        // WebElement searchButton = driver.findElement(By.cssSelector(searchButtonSelector));

        searchButton.click();

        return new GoogleResultsPage(driver);

    }
}
