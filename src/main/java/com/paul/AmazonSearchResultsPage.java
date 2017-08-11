package com.paul;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.OutputType;

import org.openqa.selenium.TakesScreenshot;

import java.util.List;

public class AmazonSearchResultsPage {

    private WebDriver driver;

    final static public String title = "Show results for";

    @FindBy(css="#leftNavContainer")
    private WebElement showResultsFor;

    @FindBy(css="#result_0 img")
    private WebElement firstResult;

    @FindBy(css=".result-list-parent-container ul li a")
    private List<WebElement> results;

    public AmazonSearchResultsPage(WebDriver driver) {

        this.driver = driver;

        // Initialise Elements
        PageFactory.initElements(driver, this);

    }

    public boolean verify() {

        // WebElement h3 = showResultsFor.findElement(By.cssSelector("h3"));

        String actual =  showResultsFor.getText().substring(0,title.length()); //h3.getText();

        System.out.println(">>> big black guy: " + actual);

        return (actual.equals(title));

    }

    public AmazonDetailPage clickFirstResult() {

        firstResult.click();

        return new AmazonDetailPage(driver);

    }

    public void clickResult(int n) {

        results.get(n).click();

    }

}
