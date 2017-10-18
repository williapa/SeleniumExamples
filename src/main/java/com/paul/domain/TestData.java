package com.paul.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@DynamoDBTable(tableName="TestData")
public class TestData {

    private String url;
    private String methodName;
    private Set<Long> assertions;
    private int lastPassed;
    private boolean forceRun;

    public TestData() {}

    public TestData(String url, String methodName, Set<Long> assertions, int lastPassed, boolean forceRun) {
        this.url = url;
        this.methodName = methodName;
        this.assertions = assertions;
        this.lastPassed = lastPassed;
        this.forceRun = forceRun;
    }

    @DynamoDBAttribute(attributeName = "url")
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @DynamoDBHashKey(attributeName = "methodName")
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @DynamoDBAttribute(attributeName = "assertions")
    public Set<Long> getAssertions() {
        return assertions;
    }
    public void setAssertions(Set<Long> assertions) {
        this.assertions = assertions;
    }

    @DynamoDBAttribute(attributeName = "lastPassed")
    public int getLastPassed() {
        return lastPassed;
    }
    public void setLastPassed(int lastPassed) {
        this.lastPassed = lastPassed;
    }

    @DynamoDBAttribute(attributeName = "forceRun")
    public boolean getForceRun() {
        return forceRun;
    }
    public void setLastPassed(boolean forceRun) {
        this.forceRun = forceRun;
    }

    public boolean skippable() {
        Date passedAt = new Timestamp(lastPassed);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        if(cal.getTime().after(passedAt) || forceRun) {
            return false;
        }
        return true;
    }
}
