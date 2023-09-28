/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;

import java.util.HashMap;

import com.cms.Feedback;
import com.cms.entity.Web;
import com.cms.routes.RouteMapping;


/**
 * Controller - 网站信息
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/web")
public class WebController extends BaseController {
	
	/**
	 * 编辑
	 */
	public void index(){
		Web web = new Web().dao().findBySiteId(getCurrentSite().getId());
		setAttr("web", web);
		render(getView("web/index"));
	}
	
	/**
	 * 修改
	 */
	public void update(){
		Web web =  getModel(Web.class,"",true); 
		web.update();
		renderJson(Feedback.success(new HashMap<>()));
	}
	
}
