package com.paul;

import org.openqa.selenium.WebDriver;

public class AmazonCommon {

    private WebDriver driver;

    public AmazonCommon AmazonCommon(WebDriver driver) {
        this.driver = driver;
        return this;
    }

}
