package com.paul.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AmazonDetailPage extends AmazonCommon {

    @FindBy(css="#one-click-button")
    private WebElement oneClickButton;

    @FindBy(css="#sponsored-products-dp_feature_div")
    private WebElement sponsoredProductsCarousel;

    @FindBy(css="#sponsored-products-dp_feature_div .a-carousel-left a")
    private WebElement sponsoredProductsLeftButton;

    @FindBy(css="#sponsored-products-dp_feature_div .a-carousel-card")
    private List<WebElement> sponsoredSlides;

    @FindBy(css="#sponsored-products-dp_feature_div .a-carousel-right a")
    private WebElement sponsoredProductsRightButton;

    @FindBy(css="#sims-consolidated-2_feature_div")
    private WebElement customersAlsoBoughtCarousel;

    @FindBy(css="#sims-consolidated=2_feature_div .a-carousel-left a")
    private WebElement customersAlsoBoughtLeftButton;

    @FindBy(css="#sims-consolidated=2_feature_div .a-carousel-card")
    private List<WebElement> customersAlsoBoughtSlides;

    @FindBy(css="#sims-consolidated=2_feature_div .a-carousel-right a")
    private WebElement customersAlsoBoughtRightButton;

    private WebDriver driver;

    public AmazonDetailPage(WebDriver driver) {
        super(driver);
        // Initialise Elements
        PageFactory.initElements(driver, this);

    }

    public AmazonSignInPage buyWithoutBeingSignedIn() {
        oneClickButton.click();
        return new AmazonSignInPage(driver);
    }

    public boolean verifySponsoredLeftButtonWorks() {
        List<WebElement> oldSlides = sponsoredSlides;
        sponsoredProductsLeftButton.click();
        int size = oldSlides.size();
        for(int i = 0; i < oldSlides.size(); i++){
            oldSlides.get(i).getText();
        }
        return true;
    }

}
