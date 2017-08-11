package com.paul;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AmazonDetailPage {

    private WebDriver driver;

    @FindBy(css="#one-click-button")
    private WebElement oneClickButton;

    public AmazonDetailPage(WebDriver driver) {

        this.driver = driver;

        // Initialise Elements
        PageFactory.initElements(driver, this);

    }

    public AmazonSignInPage buyWithoutBeingSignedIn() {

        oneClickButton.click();

        return new AmazonSignInPage(driver);
    }

}
