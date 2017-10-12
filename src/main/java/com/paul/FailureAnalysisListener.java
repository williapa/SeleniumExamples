package com.paul;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class FailureAnalysisListener extends RunListener {

    public FailureAnalysisListener() {}

    @Override
    public void testRunFinished(Result result) {
        int count = 1;
        System.out.println("You patiently waited " + result.getRunTime() + " seconds while my tests did the heavy lifting...");
        System.out.println("There were " + result.getFailureCount() + " failures out of " + result.getRunCount());
        if (result.getFailureCount() == 0) {
            System.out.println("You achieved: Continuous Integration!");
        }
        for(Failure f : result.getFailures()) {
            //log for now, write to database or upload to s3 in future
            System.out.println("failure " + count + ": ");
            System.out.println("    Test header: " + f.getTestHeader());
            System.out.println("    Exception: " + f.getException());
            System.out.println("    Trace: " + f.getTrace());
            count++;
        }
    }
}
