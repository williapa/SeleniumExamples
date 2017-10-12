package com.paul;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.lang.reflect.Field;

import java.util.Arrays;
import java.util.List;

public class GooglePage {

    private WebDriver driver;
    private static String marketplace;
    public static String[] markets = {"US", "UK", "MX"};

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
        WebElement si = getElementByMarket("searchInput");
        System.out.println("is valid: " + si.isDisplayed() + " from marketplace: " + this.marketplace);

        return si.isDisplayed() || driver.getTitle().equals("Robot Checker");
    }

    public WebElement getElementByMarket(String element) {
        Field properField = null;

        List<Field> fields = Arrays.asList(this.getClass().getDeclaredFields());
        for(Field f : fields){
            if(f.getName().equals(this.marketplace + "_" + element)) {
                properField = f;
            }
        }

        if(properField == null ) {
            try {
                properField = this.getClass().getDeclaredField(element);
            } catch (NoSuchFieldException e) {
                System.out.println("error going through private fields...");
                System.out.println(e.getMessage());
            }
        }

        properField.setAccessible(true);
        try {
            return (WebElement) properField.get(this);
        } catch (IllegalAccessException e) {
            System.out.println("error going through private fields...");
            System.out.println(e.getMessage());
            return null;
        }
    }
    // Constructor
    public GooglePage(WebDriver driver) {
        this.driver = driver;
        driver.get(US_PAGE_URL);
        this.marketplace = "US";
        // Initialise Elements
        PageFactory.initElements(driver, this);
    }

    // marketplace constructor
    public GooglePage(WebDriver driver, String market) throws Exception {
        this.driver = driver;
        if (market == "UK") {
            driver.get(UK_PAGE_URL);
            this.marketplace = "UK";
        } else if (market == "MX") {
            driver.get(MX_PAGE_URL);
            this.marketplace = "MX";
        } else {
            driver.get(US_PAGE_URL);
            this.marketplace = "US";
        }
        File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        // now copy the  screenshot to desired location using copyFile //method
        int random = (int) Math.floor(Math.random() * 101);
        FileUtils.copyFile(src, new File(System.getProperty("user.dir") + "/testScreenshots/" + random + this.marketplace + ".png"));
        // Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public void enterText(String string) {
        // WebElement searchInput = driver.findElement(By.cssSelector(searchInputSelector));
        searchInput.sendKeys(string);
    }

    public GoogleResultsPage clickSearch() {
        // WebElement searchButton = driver.findElement(By.cssSelector(searchButtonSelector));
        searchButton.click();
        return new GoogleResultsPage(driver);
    }
}
