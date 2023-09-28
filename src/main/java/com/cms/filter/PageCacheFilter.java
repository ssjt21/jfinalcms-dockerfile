package com.cms.filter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;

import com.cms.Config;
import com.cms.util.SystemUtils;

import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;

public class PageCacheFilter extends SimplePageCachingFilter {

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws  Exception {
		// TODO Auto-generated method stub
		Config config = SystemUtils.getConfig();
		if (BooleanUtils.isTrue(config.getIsCacheEnabled()) 
				&& isCache(request.getRequestURI())) {
            super.doFilter(request, response, chain);
        } else {
            chain.doFilter(request, response);
        }
	}
	
	private boolean isCache(String requestURI) {
		if (requestURI.startsWith("/api/")
				|| requestURI.startsWith("/category/")
				|| requestURI.startsWith("/content/")
				) {
			return false;
		}
     //通过正则表达式判断是否缓存该页面
     String[] cacheUrl = new String[] { 
             "^/$",//首页
             "^/[A-Za-z0-9_]+$",//栏目页
             "^/[A-Za-z0-9_]+/\\d+$" //详情页
             };
     for (String string : cacheUrl) {
         if (requestURI.matches(string)) {
             return true;
         }
     }
     return false;
 }

	
}
