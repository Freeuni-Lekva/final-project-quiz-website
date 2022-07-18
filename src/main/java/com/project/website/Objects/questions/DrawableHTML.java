package com.project.website.Objects.questions;

import com.project.website.utils.JSPParamPair;

import java.util.List;

public interface DrawableHTML {

    /**
     *
     * @return jsp filename responsible for drawing the Answerable class
     */
    String getJSP();

    /**
     *
     * @return a list of all the params to be set for the Answerable class JSP
     */
    List<JSPParamPair> getJSPParams();
}
