/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.cms.Config;
import com.cms.Feedback;
import com.cms.entity.Setup;
import com.cms.routes.RouteMapping;
import com.cms.util.CacheUtils;
import com.cms.util.SystemUtils;


/**
 * Controller - 配置
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/setup")

public class SetupController extends BaseController {
	
	/**
	 * 编辑
	 */
	public void index(){
		Config config = SystemUtils.getConfig();
		setAttr("config", config);
		render(getView("setup/index"));
	}
	
	/**
	 * 更新
	 */
	public void update(){
		Map<String,String> setupMap = new Setup().getSetupMap();
		Set<String> keys = setupMap.keySet();
		for(String key : keys){
			String value = setupMap.get(key);
			String paraValue = getPara(key);
			if(!StringUtils.equals(value, paraValue)){
				Setup setup = new Setup().dao().findByName(key);
				setup.setValue(paraValue);
				setup.update();
			}
		}
		CacheUtils.clearConfig();
		renderJson(Feedback.success(new HashMap<>()));
	}
	
}
