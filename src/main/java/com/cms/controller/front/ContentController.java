/*
 * 
 * 
 * 
 */
package com.cms.controller.front;

import com.cms.entity.Category;
import com.cms.entity.Content;
import com.cms.routes.RouteMapping;

/**
 * Controller - 内容
 * 
 * 
 * 
 */
@RouteMapping(url = "/content")
public class ContentController extends BaseController {

	/**
	 * 内容
	 */
	public void index() {
		Integer id = getParaToInt("id");
		Content content = new Content().dao().findById(id);
		setAttr("currentContent", content);
		Category category = content.getCategory();
		setAttr("currentCategory", category);
		render("/templates/"+getCurrentTemplate()+"/"+category.getDetailTemplate());
	}

	/**
	 * 点击数
	 */
	public void visits() {
		Integer id = getParaToInt("id");
		if (id == null) {
			renderJson(0L);
			return;
		}
		Content content = new Content().dao().findById(id);
		Integer visits = content.getVisits();
		visits = visits+1;
		content.setVisits(visits);
		content.update();
		renderJavascript("document.write("+visits+")");
	}
}