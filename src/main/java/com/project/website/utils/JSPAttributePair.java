package com.project.website.utils;

import java.io.Serializable;

public class JSPAttributePair implements Serializable {
    private String attributeName;
    private String attributeValue;

    public JSPAttributePair(String attributeName, String attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }
}
