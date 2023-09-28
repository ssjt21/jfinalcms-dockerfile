/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import com.cms.TemplateVariable;
import com.cms.entity.Content;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 模板指令 - 单个内容
 * 
 * 
 * 
 */
@TemplateVariable(name="content")
public class ContentDirective extends BaseDirective {
	
	/** "ID"参数名称 */
	private static final String ID_PARAMETER_NAME = "id";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "content";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
	    scope = new Scope(scope);
		Long id = getParameter(ID_PARAMETER_NAME, Long.class, scope);
		Content content = new Content().dao().findById(id);
		scope.setLocal(VARIABLE_NAME,content);
        stat.exec(env, scope, writer);
	}
	
    public boolean hasEnd() {
        return true;
    }	
}