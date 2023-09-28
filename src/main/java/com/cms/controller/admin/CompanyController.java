/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;

import java.util.HashMap;

import com.cms.Feedback;
import com.cms.entity.Company;
import com.cms.routes.RouteMapping;


/**
 * Controller - 公司信息
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/company")
public class CompanyController extends BaseController {
	
	/**
	 * 编辑
	 */
	public void index(){
		Company company = new Company().dao().findBySiteId(getCurrentSite().getId());
		setAttr("company", company);
		render(getView("company/index"));
	}
	
	/**
	 * 修改
	 */
	public void update(){
		Company company =  getModel(Company.class,"",true); 
		company.update();
		renderJson(Feedback.success(new HashMap<>()));
	}
	
}
