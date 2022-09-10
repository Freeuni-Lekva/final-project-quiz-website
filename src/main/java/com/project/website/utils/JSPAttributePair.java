package com.project.website.utils;

import java.io.Serializable;

public class JSPAttributePair implements Serializable {
    private final String attributeName;
    private final Serializable attributeValue;

    public JSPAttributePair(String attributeName, Serializable attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Serializable getAttributeValue() {
        return attributeValue;
    }
}
