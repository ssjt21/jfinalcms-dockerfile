package com.cms.util;

import javax.servlet.http.HttpServletRequest;

public class SiteUtils {

    /**
     * 获取域名
     * 
     * @return 域名
     */
    public static String getDomain(HttpServletRequest request){
        StringBuffer url = request.getRequestURL();  
        String domain = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        domain = domain.replace("https://", "").replace("http://", "");
        return domain;
    }
}
