package com.paul;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GoogleResultsPage {

    private WebDriver driver;

    // Locators
    @FindBy(css="$search a")
    private WebElement firstResult;

    // Constructor
    public GoogleResultsPage(WebDriver driver) {
        this.driver = driver;
        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public String getResult() {
        return firstResult.getAttribute("href").substring(0,22);
    }
}
