package com.paul.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Assertion")
public class Assertion {

    private Long id;
    private String selector;
    private String attribute;
    private String expected;
    private String description;

    public Assertion() {}

    public Assertion(Long id, String selector, String attribute, String expected, String description) {
        this.id = id;
        this.selector = selector;
        this.attribute = attribute;
        this.expected = expected;
        this.description = description;
    }

    @DynamoDBHashKey(attributeName = "Id")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "selector")
    public String getSelector() {
        return selector;
    }
    public void setSelector(String selector) {
        this.selector = selector;
    }

    @DynamoDBAttribute(attributeName = "attribute")
    public String getAttribute() {
        return attribute;
    }
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @DynamoDBAttribute(attributeName = "expected")
    public String getExpected() {
        return expected;
    }
    public void setExpected(String expected) {
        this.expected = expected;
    }
}
