package com.paul;

import com.paul.test.SinglePageTest;
import org.junit.runner.JUnitCore;

public class Runner {
    public static void main(String[] args) {
        JUnitCore core = new JUnitCore();
        core.addListener(new FailureAnalysisListener());
        //TODO take the class to run from args
        core.run(SinglePageTest.class);
    }
}
