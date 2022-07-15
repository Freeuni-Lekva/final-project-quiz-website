package com.project.website.utils;

import java.io.Serializable;

public class JSPParamPair implements Serializable {
    private String paramName;
    private String paramValue;

    public JSPParamPair(String paramName, String paramValue) {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    public String getParamName() {
        return paramName;
    }

    public String getParamValue() {
        return paramValue;
    }
}
