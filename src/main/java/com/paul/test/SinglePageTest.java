package com.paul.test;

import com.paul.domain.TestData;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

public class SinglePageTest extends BaseTest {

    @Test
    public void coinDeskHTC() throws Exception {
        TestData td = getTestDataByMethodName("coinDeskHTC");
        Assume.assumeFalse(td.isPassedSinceLastPush());
        boolean res = executeTest(driverFactory.getFirefoxDriver(), td);
        Assert.assertEquals(res, true);
    }

    @Test
    public void redditHomeDesktop() throws Exception {
        TestData td = getTestDataByMethodName("redditHomeDesktop");
        Assume.assumeFalse(td.isPassedSinceLastPush());
        boolean res = executeTest(driverFactory.getFirefoxDriver(), td);
        Assert.assertEquals(res, true);
    }
}
