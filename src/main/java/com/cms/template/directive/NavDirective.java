/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import com.cms.TemplateVariable;
import com.cms.entity.Nav;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 模板指令 - 单个导航
 * 
 * 
 * 
 */
@TemplateVariable(name="nav")
public class NavDirective extends BaseDirective {
	
	/** "ID"参数名称 */
	private static final String ID_PARAMETER_NAME = "id";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "category";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
	    scope = new Scope(scope);
		Long id = getParameter(ID_PARAMETER_NAME, Long.class, scope);
		Nav nav = new Nav().dao().findById(id);
		scope.setLocal(VARIABLE_NAME,nav);
        stat.exec(env, scope, writer);
	}

    public boolean hasEnd() {
        return true;
    }
    
    
}