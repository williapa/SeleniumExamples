package com.paul.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class YahooResultsPage {

    private WebDriver driver;

    // Locators
    @FindBy(css="#web h3 a")
    private WebElement firstResult;

    // Constructor
    public YahooResultsPage(WebDriver driver) {

        this.driver = driver;

        //Initialise Elements
        PageFactory.initElements(driver, this);

    }

    public String getResult() {

        //WebElement firstResult = driver.findElement(By.cssSelector(firstResultSelector));

        return firstResult.getAttribute("href").substring(0,22);

    }

}
