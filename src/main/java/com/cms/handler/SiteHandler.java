package com.cms.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.cms.UrlRewriteWrappedRequest;
import com.cms.entity.Company;
import com.cms.entity.Site;
import com.cms.entity.Web;
import com.cms.util.SiteUtils;
import com.jfinal.handler.Handler;
import com.jfinal.plugin.activerecord.Db;

/**
* 站点控制器
*/
public class SiteHandler extends Handler {
	
    /** 不包含 */
    private List<String> urlExcludes = new ArrayList<String>(){{
        add("/category");
        add("/content");
        add("/div");
		add("/guestbook");
		add("/search");
    }};
	
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		if(target.startsWith("/admin/")){
			next.handle(target, request, response, isHandled);
			return;
		}
		String domain = SiteUtils.getDomain(request);
		String[] urls = target.split("/");
		Integer siteNum = Db.queryInt("select count(*) from cms_site where domain=?",domain);
		if(siteNum == null || siteNum<1){
			if(urls.length<1){
				//默认站点
				Site site = new Site().dao().findDefault();
				wrappedRequest(site, target,  request,  response, isHandled);
				return;
			}
			//无域名带URL目录
			String cat = urls[1];
			if(!urlExcludes.contains("/"+cat)){
				Site site = new Site().dao().findFirst("select * from cms_site where type=? and cat = ?",Site.Type.CAT.ordinal(),cat);
				if(site != null){
					target = target.substring(("/"+cat).length());
					wrappedRequest(site, target,  request,  response, isHandled);
					return;
				}
			}
		}else{
			//有域名
			Site site = new Site().dao().findFirst("select * from cms_site where type=? and domain=?",Site.Type.DOMAIN.ordinal(),domain);
			wrappedRequest(site, target,  request,  response, isHandled);
			return;
		}
		//默认站点
		Site site = new Site().dao().findDefault();
		wrappedRequest(site, target,  request,  response, isHandled);
		return;
	}
	
	//包装request
	public void wrappedRequest(Site site,String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled){
		request.setAttribute(Site.CURRENT_SITE, site);
		request.setAttribute(Web.CURRENT_WEB, new Web().dao().findBySiteId(site.getId()));
		request.setAttribute(Company.CURRENT_COMPANY, new Company().dao().findBySiteId(site.getId()));
		HashMap overridenParameters = new HashMap();
		UrlRewriteWrappedRequest urlRewriteWrappedRequest = new UrlRewriteWrappedRequest(request,overridenParameters);
		next.handle(target, urlRewriteWrappedRequest, response, isHandled);
	}
}
