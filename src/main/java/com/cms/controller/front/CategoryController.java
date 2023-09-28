package com.cms.controller.front;

import com.cms.entity.Category;
import com.cms.entity.Model;
import com.cms.routes.RouteMapping;

/**
 * Controller - 栏目
 * 
 * 
 * 
 */
@RouteMapping(url = "/category")
public class CategoryController extends BaseController {

	/**
	 * 栏目
	 */
	public void index() {
		Integer id = getParaToInt("id");
		Category category = new Category().dao().findById(id);
		setAttr("currentCategory", category);
		Integer pageNumber = getParaToInt("pageNumber");
		if(pageNumber==null){
			pageNumber = 1;
		}
		setAttr("pageNumber", pageNumber);
		Model model = category.getModel();
		if(model.getType() == Model.Type.PAGE.ordinal()){
			render("/templates/"+getCurrentTemplate()+"/"+category.getDetailTemplate());
		}else{
			render("/templates/"+getCurrentTemplate()+"/"+category.getListTemplate());
		}
	}
}
