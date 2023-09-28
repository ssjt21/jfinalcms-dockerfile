package com.cms.controller.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.cms.CommonAttribute;
import com.cms.entity.Admin;
import com.cms.entity.Site;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;

/**
 * Controller - 基类
 * 
 * 
 * 
 */
public class BaseController extends Controller{

	/** 每页记录数 */
	protected static final int PAGE_SIZE = 10;
	
	/** 列表查询Cookie名称 */
	private static final String LIST_QUERY_COOKIE_NAME = "listQuery";
	
   /**
     * 获取当前站点
     * 
     * @return 当前站点
     */
    @NotAction
    protected Site getCurrentSite() {
        Site currentSite = (Site) getSession().getAttribute(Site.ADMIN_SESSION_SITE);
        return currentSite;
    }
	
	/**
	 * 获取当前管理员
	 * 
	 * @return 当前管理员
	 */
	@NotAction
	protected Admin getCurrentAdmin() {
		Admin currentAdmin = (Admin) getSession().getAttribute(Admin.SESSION_ADMIN);
		return currentAdmin;
	}
	
    /**
     * 获取页面
     * 
     * @return 页面
     */
	@NotAction
    public String getView(String view){
        return CommonAttribute.ADMIN_PATH+view+CommonAttribute.VIEW_EXTENSION;
    }
	
	/**
	 * 设置列表参数
	 * 
	 */
	@NotAction
	public void setListQuery(){
		Map<String,String[]> paraMap = getParaMap();
		String listQuery = "";
		if(paraMap!=null && !paraMap.isEmpty()){
			for(String key : paraMap.keySet()){
				String [] values = paraMap.get(key);
				if(ArrayUtils.isNotEmpty(values)){
					for(String value : values){
						listQuery+="&"+key+"="+value;
					}
				}
			}
		}
		if(StringUtils.isNotBlank(listQuery)){
			listQuery = listQuery.substring(1);
			try {
				setCookie(LIST_QUERY_COOKIE_NAME, URLEncoder.encode(listQuery, "UTF-8"),10 * 60,"");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取列表参数
	 * 
	 * @return 列表参数
	 */
	@NotAction
	public String getListQuery(String url){
		String listQuery = null;
		try {
			String cookieListQuery = getCookie(LIST_QUERY_COOKIE_NAME);
			if(StringUtils.isNotBlank(cookieListQuery)){
				listQuery = URLDecoder.decode(cookieListQuery, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(url) && StringUtils.isNotEmpty(listQuery)){
			if (StringUtils.startsWith(listQuery, "?")) {
				listQuery = listQuery.substring(1);
			}
			if (StringUtils.contains(url, "?")) {
				url = url + "&" + listQuery;
			} else {
				url = url + "?" + listQuery;
			}
			removeCookie(LIST_QUERY_COOKIE_NAME);
		}
		return url;
	}
}