/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import com.cms.TemplateVariable;
import com.cms.entity.Category;
import com.cms.entity.Nav;
import com.cms.entity.Site;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.List;

/**
 * 模板指令 - 导航列表
 * 
 * 
 * 
 */
@TemplateVariable(name="nav_root_list")
public class NavRootListDirective extends BaseDirective {

	/** 变量名称 */
	private static final String VARIABLE_NAME = "navs";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
	    Site currentSite = getCurrentSite(scope);
	    scope = new Scope(scope);
	    Integer siteId = currentSite.getId();
		Integer start = getStart(scope);
		Integer count = getCount(scope);
		List<Nav> navs = new Nav().dao().findRoots(start,count,siteId);
		scope.setLocal(VARIABLE_NAME,navs);
        stat.exec(env, scope, writer);
	}
	
    public boolean hasEnd() {
        return true;
    }	
}