/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.cms.entity.Admin;
import com.cms.entity.Site;
import com.cms.routes.RouteMapping;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * Controller - 首页
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/index")

public class IndexController extends BaseController {


	/**
	 * 首页
	 */
	public void index() {
		//站点
		Integer siteId = getParaToInt("siteId");
	    if(siteId == null){
	    	Admin currentAdmin = getCurrentAdmin();
	    	if(BooleanUtils.isFalse(currentAdmin.getRole().getIsSystem())
	    			&& CollectionUtils.isEmpty(currentAdmin.getRole().getSiteIds())){
	    		renderHtml("<script>alert('用户没有授权站点');history.back();</script>");
	    		return;
	    	}
	        Site defaultSite = new Site().dao().findDefault();
	        if(BooleanUtils.isTrue(currentAdmin.getRole().getIsSystem())
	        		|| currentAdmin.getRole().getSiteIds().contains(defaultSite.getId())){
	        	siteId = defaultSite.getId();
	        }else{
	        	siteId = currentAdmin.getRole().getSiteIds().get(0);
	        }
	    }
    	Site currentSite = new Site().dao().findById(siteId);
    	getSession().setAttribute(Site.ADMIN_SESSION_SITE, currentSite);
	    //统计
		setAttr("contentCount", Db.queryInt("select count(*) from cms_content"));
		setAttr("formCount", Db.queryInt("select count(*) from cms_div"));
		setAttr("guestbookCount", Db.queryInt("select count(*) from cms_guestbook"));
		setAttr("siteCount", Db.queryInt("select count(*) from cms_site"));
		String guestbookItemSql = "";
		String contentItemSql = "";
		for(int i=0;i<=6;i++){
			Date date = DateUtils.addDays(new Date(), -i);
			if(i>0){
				guestbookItemSql+=",";
				contentItemSql+=",";
			}
			guestbookItemSql += "(select count(*) from cms_guestbook where createDate <= '"+DateFormatUtils.format(date, "yyyy-MM-dd")+" 23:59:59') as '"+DateFormatUtils.format(date, "MM-dd")+"'";
			contentItemSql += "(select count(*) from cms_content where createDate <= '"+DateFormatUtils.format(date, "yyyy-MM-dd")+" 23:59:59') as '"+DateFormatUtils.format(date, "MM-dd")+"'";
		}
		String guestbookSql = "select "+guestbookItemSql;
		Record guestbookRecord = Db.findFirst(guestbookSql);
		String [] guestbookColumnNames = guestbookRecord.getColumnNames();
		Object [] guestbookColumnValues = guestbookRecord.getColumnValues();
		setAttr("guestbookTime", "'"+StringUtils.join(guestbookColumnNames,"','")+"'");
		setAttr("guestbookValue", StringUtils.join(guestbookColumnValues,","));
		
		String contentSql = "select "+contentItemSql;
		Record contentRecord = Db.findFirst(contentSql);
		String [] contentColumnNames = contentRecord.getColumnNames();
		Object [] contentColumnValues = contentRecord.getColumnValues();
		setAttr("contentTime", "'"+StringUtils.join(contentColumnNames,"','")+"'");
		setAttr("contentValue", StringUtils.join(contentColumnValues,","));
		
		render(getView("index/index"));
	}
}