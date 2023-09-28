/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import java.util.List;

import com.cms.TemplateVariable;
import com.cms.entity.Category;
import com.cms.entity.Site;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 模板指令 - 父栏目列表
 * 
 * 
 * 
 */
@TemplateVariable(name="category_parent_list")
public class CategoryParentListDirective extends BaseDirective {

	/** "文章分类ID"参数名称 */
	private static final String CATEGORY_ID_PARAMETER_NAME = "categoryId";
	
	/** "是否递归"参数名称 */
	private static final String RECURSIVE_PARAMETER_NAME = "recursive";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "categorys";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
	    Site currentSite = getCurrentSite(scope);
	    scope = new Scope(scope);
	    Integer siteId = currentSite.getId();
	    Integer categoryId = getParameter(CATEGORY_ID_PARAMETER_NAME, Integer.class, scope);
		Boolean recursive = getParameter(RECURSIVE_PARAMETER_NAME, Boolean.class, scope);
		Integer start = getStart(scope);
		Integer count = getCount(scope);
		List<Category> categorys = new Category().dao().findParents(categoryId, recursive != null ? recursive : false, start, count,siteId);
		scope.setLocal(VARIABLE_NAME,categorys);
        stat.exec(env, scope, writer);
	}
	
    public boolean hasEnd() {
        return true;
    }	
}