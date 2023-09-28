/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import java.util.List;

import com.cms.TemplateVariable;
import com.cms.entity.Menu;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 模板指令 - 根菜单列表
 * 
 * 
 * 
 */
@TemplateVariable(name="menu_root_list")
public class MenuRootListDirective extends BaseDirective {

	/** 变量名称 */
	private static final String VARIABLE_NAME = "menus";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
	    scope = new Scope(scope);
		List<Menu> menus = new Menu().dao().findRoots();
		scope.setLocal(VARIABLE_NAME,menus);
        stat.exec(env, scope, writer);
	}
	
    public boolean hasEnd() {
        return true;
    }	
}