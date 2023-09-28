/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import java.util.List;

import com.cms.TemplateVariable;
import com.cms.entity.Site;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 模板指令 - 站点列表
 * 
 * 
 * 
 */
@TemplateVariable(name="site_list")
public class SiteListDirective extends BaseDirective {

	/** 变量名称 */
	private static final String VARIABLE_NAME = "sites";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
		scope = new Scope(scope);
		List<Site> sites = new Site().dao().findAll();
		scope.setLocal(VARIABLE_NAME,sites);
        stat.exec(env, scope, writer);
	}
	
    public boolean hasEnd() {
        return true;
    }	
}