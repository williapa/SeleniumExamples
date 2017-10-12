package com.paul;

import org.junit.runner.JUnitCore;

public class Runner {
    public static void main(String[] args) {
        JUnitCore core= new JUnitCore();
        core.addListener(new FailureAnalysisListener());
        core.run(SampleTest.class);
    }
}
