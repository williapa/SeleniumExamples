package com.paul.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Set;

@DynamoDBTable(tableName="TestData")
public class TestData {

    private String url;
    private String methodName;
    private Set<Long> assertions;
    private boolean passedSinceLastPush;

    public TestData() {}

    public TestData(String url, String methodName, Set<Long> assertions, boolean passedSinceLastPush) {
        this.url = url;
        this.methodName = methodName;
        this.assertions = assertions;
        this.passedSinceLastPush = passedSinceLastPush;
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

    @DynamoDBAttribute(attributeName = "passedSinceLastPush")
    public boolean isPassedSinceLastPush() {
        return passedSinceLastPush;
    }
    public void setPassedSinceLastPush(boolean passedSinceLastPush) {
        this.passedSinceLastPush = passedSinceLastPush;
    }
}
