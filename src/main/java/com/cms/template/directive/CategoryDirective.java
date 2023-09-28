/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import com.cms.TemplateVariable;
import com.cms.entity.Category;
import com.cms.entity.Site;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 模板指令 - 单个栏目
 * 
 * 
 * 
 */
@TemplateVariable(name="category")
public class CategoryDirective extends BaseDirective {
	
	/** "ID"参数名称 */
	private static final String ID_PARAMETER_NAME = "id";

	/** "名称"参数名称 */
	private static final String NAME_PARAMETER_NAME = "name";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "category";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
		Site currentSite = getCurrentSite(scope);
	    scope = new Scope(scope);
		Integer siteId = currentSite.getId();
		Long id = getParameter(ID_PARAMETER_NAME, Long.class, scope);
		String name = getParameter(NAME_PARAMETER_NAME, String.class, scope);
		Category category = null;
		if(id!=null){
			category = new Category().dao().findById(id);
		}else{
			category = new Category().dao().findByName(name,siteId);
		}
		scope.setLocal(VARIABLE_NAME,category);
        stat.exec(env, scope, writer);
	}

    public boolean hasEnd() {
        return true;
    }
    
    
}