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
 * 模板指令 - 根栏目列表
 * 
 * 
 * 
 */
@TemplateVariable(name="category_root_list")
public class CategoryRootListDirective extends BaseDirective {

	/** 变量名称 */
	private static final String VARIABLE_NAME = "categorys";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
	    Site currentSite = getCurrentSite(scope);
	    scope = new Scope(scope);
	    Integer siteId = currentSite.getId();
		Integer start = getStart(scope);
		Integer count = getCount(scope);
		List<Category> categorys = new Category().dao().findRoots(start,count,siteId);
		scope.setLocal(VARIABLE_NAME,categorys);
        stat.exec(env, scope, writer);
	}
	
    public boolean hasEnd() {
        return true;
    }	
}