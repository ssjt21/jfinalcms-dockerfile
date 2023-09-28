package com.cms;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class UrlRewriteWrappedRequest extends HttpServletRequestWrapper {

    HashMap overridenParameters;

    public UrlRewriteWrappedRequest(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }

    public UrlRewriteWrappedRequest(HttpServletRequest httpServletRequest,
                                    HashMap overridenParameters) {
        super(httpServletRequest);
        this.overridenParameters = overridenParameters;
    }

    public Enumeration getParameterNames() {
        if (overridenParameters != null) {
            List keys = Collections.list(super.getParameterNames());
            keys.addAll(overridenParameters.keySet());
            return Collections.enumeration(keys);
        }
        return super.getParameterNames();
    }

    public Map getParameterMap() {
        if (overridenParameters != null) {
            Map superMap = super.getParameterMap();
            //superMap is an unmodifiable map, hence creating a new one.
            Map overriddenMap = new HashMap(superMap.size() + overridenParameters.size());
            overriddenMap.putAll(superMap);
            overriddenMap.putAll(overridenParameters);
            return overriddenMap;
        }
        return super.getParameterMap();
    }

    public String[] getParameterValues(String s) {
        if (overridenParameters != null && overridenParameters.containsKey(s)) {
            return (String[]) overridenParameters.get(s);
        }
        return super.getParameterValues(s);
    }

    public String getParameter(String s) {
        if (overridenParameters != null && overridenParameters.containsKey(s)) {
            String[] values = (String[]) overridenParameters.get(s);
            if (values == null || values.length == 0) {
                return null;
            } else {
                return values[0];
            }
        }
        return super.getParameter(s);
    }
}
