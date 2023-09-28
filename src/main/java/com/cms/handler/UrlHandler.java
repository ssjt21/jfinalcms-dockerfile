package com.cms.handler;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cms.Config;
import com.cms.entity.Tag;
import com.cms.util.SystemUtils;
import org.apache.commons.lang.StringUtils;

import com.cms.UrlRewriteWrappedRequest;
import com.cms.entity.Category;
import com.cms.entity.Site;
import com.jfinal.handler.Handler;

/**
* UrlHandler.
*/
public class UrlHandler extends Handler {
	
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		if ("/".equals(target) 
				|| target.startsWith("/admin/")
				|| target.startsWith("/category/")
				|| target.startsWith("/content/")
				|| target.startsWith("/div/")
				|| target.startsWith("/guestbook/")
				|| target.startsWith("/search/")
				|| target.startsWith("/ajax/")
				|| target.startsWith("/page/")
				) {
			next.handle(target, request, response, isHandled);
			return;
		}
		Site currentSite = (Site) request.getAttribute(Site.CURRENT_SITE);
		HashMap overridenParameters = new HashMap();
		//模式
		Config config = SystemUtils.getConfig();
		if(config.getSiteModel()==Config.SiteModel.REWRITE.ordinal()){
			target = target.replace(".html","");
		}else if(config.getSiteModel()==Config.SiteModel.HTML.ordinal()){
			if(target.endsWith(".html")){
				next.handle(target, request, response, isHandled);
				return;
			}
		}
		//tag start
		if(target.startsWith("/tag/")){
			String tagCat = target.split("/")[2];
			Tag tag = new Tag().dao().findByCat(tagCat);
			String newTarget = "";
			if(tag!=null){
				newTarget = "/tag";
				overridenParameters.put("id", new String[]{tag.getId()+""});
			}
			if(StringUtils.isNotBlank(newTarget)){
				target = newTarget;
			}
			UrlRewriteWrappedRequest urlRewriteWrappedRequest = new UrlRewriteWrappedRequest(request,overridenParameters);
			next.handle(target, urlRewriteWrappedRequest, response, isHandled);
			return;
		}
		//tag end
		//URL目录: /product  /product/{id}
		String[] urls = target.split("/");
		request.setAttribute("target", "/"+urls[1]);
		int urlsLength = urls.length;
		String newTarget = "";
		if(urlsLength==2){
			String categoryCat = urls[1];
			Category category = new Category().dao().findByCat(categoryCat,currentSite.getId());
			if(category!=null){
				newTarget = "/category";
				overridenParameters.put("id", new String[]{category.getId()+""});
			}
		}else if(urlsLength==3){
			String categoryCat = urls[1];
			String contentId = urls[2];
			Category category = new Category().dao().findByCat(categoryCat,currentSite.getId());
			if(category!=null){
				newTarget = "/content";
				overridenParameters.put("id", new String[]{contentId+""});
			}
		}
		if(StringUtils.isNotBlank(newTarget)){
			target = newTarget;
		}
		UrlRewriteWrappedRequest urlRewriteWrappedRequest = new UrlRewriteWrappedRequest(request,overridenParameters);
		next.handle(target, urlRewriteWrappedRequest, response, isHandled);
	}
}
