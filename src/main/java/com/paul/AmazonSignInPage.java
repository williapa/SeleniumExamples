package com.paul;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AmazonSignInPage {

    private WebDriver driver;

    final public static String title = "Amazon Sign In";

    @FindBy(css="#ap_email")
    private WebElement emailInput;

    @FindBy(css="#ap_password")
    private WebElement passwordInput;

    @FindBy(css="#signInSubmit")
    private WebElement signInButton;

    @FindBy(css="#signUpButton")
    private WebElement signUpButton;

    public AmazonSignInPage(WebDriver driver) {

        this.driver = driver;

        // Initialise Elements
        PageFactory.initElements(driver, this);

    }

    public boolean verify() {

        String actualTitle = driver.getTitle();

        return (actualTitle.equals(title));
    }

    public AmazonHomePage SignIn(String email, String password) {

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        signInButton.click();

        return new AmazonHomePage(driver);

    }

}
