package com.cms.controller.front;

import com.cms.entity.Tag;
import com.cms.routes.RouteMapping;

/**
 * Controller - 标签
 * 
 * 
 * 
 */
@RouteMapping(url = "/tag")
public class TagController extends BaseController {
	
	/**
	 * 标签
	 */
	public void index() {
		Integer id = getParaToInt("id");
		Tag tag = new Tag().dao().findById(id);
		if(tag==null){
			renderError(404);
			return;
		}
		setAttr("currentTag",tag);
		render("/templates/"+getCurrentTemplate()+"/tag.html");
	}
}





