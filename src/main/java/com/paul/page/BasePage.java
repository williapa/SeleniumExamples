package com.paul.page;

import com.paul.domain.Market;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public abstract class BasePage {

    public WebElement getElementByMarket(String element, Market market) {
        Field properField = null;
        List<Field> fields = Arrays.asList(this.getClass().getDeclaredFields());
        for (Field f : fields){
            if (f.getName().equals(market.getMarket() + "_" + element)) {
                properField = f;
            }
        }
        if (properField == null) {
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

    public void screenAndInit(WebDriver driver, Market market, Object page) throws Exception {
        File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        // now copy the screenshot to desired location using copyFile method
        int random = (int) Math.floor(Math.random() * 101);
        String filename = System.getProperty("user.dir") + "/testScreenshots/" + random + market.getMarket() + ".png";
        FileUtils.copyFile(src, new File(filename));
        // Initialise Elements
        PageFactory.initElements(driver, page);
    }


}
