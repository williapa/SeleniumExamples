package com.paul.test;

import org.junit.Test;

public class SinglePageTest extends BaseTest {

    @Test
    public void coinDeskHTC() throws Exception {
        execute(getTestDataByMethodName(methodName()));
    }

    @Test
    public void redditHomeDesktop() throws Exception {
        execute(getTestDataByMethodName(methodName()));
    }
}
