package com.paul;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

/* use this class to manage the driver settings and profiles, including things like mimicking devices */
public class DriverFactory {

    public DriverFactory() {
    }

    private static WebDriver getFirefoxDriver(String userAgent) {
        FirefoxProfile profile = new FirefoxProfile();
        //Change User Agent to HTC ONE M9
        profile.setPreference("general.useragent.override", userAgent);
        return new FirefoxDriver(profile);
    }

    private static WebDriver getPhantomJSDriver(String userAgent) {
        DesiredCapabilities sCaps = new DesiredCapabilities();
        // Modern pages are built on javascript!
        sCaps.setJavascriptEnabled(true);
        // We need to see what's going on to debug tests.
        sCaps.setCapability("takesScreenshot", true);
        // User Agent lets us imitate mobile devices like phones and tablets
        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent", userAgent);
        //we are testing our own fucking pages, certificate issues are probably handled in prod (asterisk?)
        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] {
                "--web-security=false",
                "--ssl-protocol=any",
                "--ignore-ssl-errors=true",
                "--webdriver-loglevel=WARN"
        });
        WebDriver phantom = new PhantomJSDriver(sCaps);
        phantom.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        Dimension bigger = new Dimension(1920, 1080);
        phantom.manage().window().setSize(bigger);
        return phantom;
    }

    public WebDriver getPhantomDriver() {
        return new PhantomJSDriver();
    }

    public WebDriver getAndroid4Driver(boolean headless) {
        return (!headless) ? getFirefoxDriver(UserAgents.ANDROID_4_0_USER_AGENT) : getPhantomJSDriver(UserAgents.ANDROID_4_0_USER_AGENT);
    }

    public WebDriver getFirefoxDriver() {
        return new FirefoxDriver();
    }

    public WebDriver getHTCDriver(boolean headless) {
        return (!headless) ? getFirefoxDriver(UserAgents.HTC_ONE_M9) : getPhantomJSDriver(UserAgents.HTC_ONE_M9);
    }

    public WebDriver getChromeDriver() {
        return new ChromeDriver();
    }
}
