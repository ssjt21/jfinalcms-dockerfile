/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import java.util.List;

import com.cms.TemplateVariable;
import com.cms.entity.Content;
import com.cms.entity.Site;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 模板指令 - 内容列表
 * 
 * 
 * 
 */
@TemplateVariable(name="content_list")
public class ContentListDirective extends BaseDirective {

	/** "栏目ID"参数名称 */
	private static final String CATEGORY_ID_PARAMETER_NAME = "categoryId";
	
	/** 变量名称 */
	private static final String VARIABLE_NAME = "contents";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
	    Site currentSite = getCurrentSite(scope);
	    scope = new Scope(scope);
	    Integer siteId = currentSite.getId();
	    Integer categoryId = getParameter(CATEGORY_ID_PARAMETER_NAME, Integer.class, scope);
		Integer start = getStart(scope);
		Integer count = getCount(scope);
		String condition = getCondition(scope);
		String orderBy = getOrderBy(scope);
		List<Content> contents = new Content().dao().findList(categoryId,condition,null,null, orderBy,start,count,siteId);
		scope.setLocal(VARIABLE_NAME,contents);
        stat.exec(env, scope, writer);
	}
	
    public boolean hasEnd() {
        return true;
    }	
}