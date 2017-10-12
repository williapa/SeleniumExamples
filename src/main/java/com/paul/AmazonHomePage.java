package com.paul;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.*;

public class AmazonHomePage {
    private WebDriver driver;

    final public static String title = "Amazon.com: Online Shopping for Electronics, Apparel, Computers, Books, DVDs & more";

    // Locators
    @FindBy(css="#twotabsearchtextbox")
    private WebElement searchInput;

    @FindBy(css=".nav-search-submit input")
    private WebElement searchButton;

    @FindBy(css=".first-carousel")
    private WebElement firstCarousel;

    @FindBy(css=".desktop-row")
    private List<WebElement> desktopRows;

    @FindBy(css="#nav-link-accountList")
    private WebElement navLinkAccountList;

    // Page URL
    private static String PAGE_URL="https://www.amazon.com";

    public AmazonHomePage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);
        // Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public boolean verify() {
        String actualTitle = driver.getTitle();
        return actualTitle.equals(title);
    }

    public void enterSearchText(String search) {
        searchInput.sendKeys(search);
    }

    public AmazonSearchResultsPage clickSearch() {
        searchButton.click();
        return new AmazonSearchResultsPage(driver);
    }

    public AmazonSignInPage clickSignIn() {
        navLinkAccountList.click();
        return new AmazonSignInPage(driver);
    }
}
