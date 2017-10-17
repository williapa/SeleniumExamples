package com.paul;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.paul.domain.TestData;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.List;
import java.util.stream.Collectors;

public class FailureAnalysisListener extends RunListener {
    protected DynamoDBMapper mapper;
    private List<TestData> testData;

    public FailureAnalysisListener() {
        mapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .build());
        testData = mapper.scan(TestData.class, new DynamoDBScanExpression());
    }

    @Override
    public void testRunFinished(Result result) {
        int count = 1;
        System.out.println("You patiently waited " + result.getRunTime()/1000 + " seconds while my tests did the heavy lifting...");
        System.out.println("There were " + result.getFailureCount() + " failures out of " + result.getRunCount());
        if (result.getFailureCount() == 0) {
            System.out.println("You achieved: Continuous Integration!");
        }
        for(Failure f : result.getFailures()) {
            //log for now, write to database in future
            System.out.println("failure " + count + ": ");
            System.out.println("    Test header: " + f.getDescription().getMethodName());
            System.out.println("    Exception: " + f.getException());
            System.out.println("    Trace: " + f.getTrace());
            count++;
            //filter that
            List<TestData> updateTest = testData.stream()
                    .filter(testData -> !f.getDescription().getMethodName().equals(testData.getMethodName()))
                    .collect(Collectors.toList());
            testData = updateTest;
        }
        // now for every test left in the collection, save the last passed date as now.
        for(TestData td : testData) {
            td.setPassedSinceLastPush(true);
            mapper.save(td);
        }
    }
}
