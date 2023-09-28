package com.cms.controller.admin;


import java.util.HashMap;

import com.cms.Feedback;
import com.cms.routes.RouteMapping;
import com.cms.util.CacheUtils;


/**
 * Controller - 缓存
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/cache")
public class CacheController extends BaseController {
	
	/**
	 * 清除缓存
	 */
    public void delete() {
		CacheUtils.clearAll();
		renderJson(Feedback.success(new HashMap<>()));
	}

}