/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import com.cms.TemplateVariable;
import com.cms.entity.Nav;
import com.cms.entity.Site;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.List;

/**
 * 模板指令 - 父导航列表
 * 
 * 
 * 
 */
@TemplateVariable(name="nav_parent_list")
public class NavParentListDirective extends BaseDirective {

	/** "导航ID"参数名称 */
	private static final String NAV_ID_PARAMETER_NAME = "navId";
	
	/** "是否递归"参数名称 */
	private static final String RECURSIVE_PARAMETER_NAME = "recursive";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "navs";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
	    Site currentSite = getCurrentSite(scope);
	    scope = new Scope(scope);
	    Integer siteId = currentSite.getId();
	    Integer navId = getParameter(NAV_ID_PARAMETER_NAME, Integer.class, scope);
		Boolean recursive = getParameter(RECURSIVE_PARAMETER_NAME, Boolean.class, scope);
		Integer start = getStart(scope);
		Integer count = getCount(scope);
		List<Nav> navs = new Nav().dao().findParents(navId, recursive != null ? recursive : false, start, count,siteId);
		scope.setLocal(VARIABLE_NAME,navs);
        stat.exec(env, scope, writer);
	}
	
    public boolean hasEnd() {
        return true;
    }	
}