/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import com.cms.TemplateVariable;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 模板指令 - SQL分页
 * 
 * 
 * 
 */
@TemplateVariable(name="sql_page")
public class SqlPageDirective extends BaseDirective {

    /** "页码"参数名称 */
    private static final String PAGE_NUMBER_PARAMETER_NAME = "pageNumber";
    
    /** "每页记录数"参数名称 */
    private static final String PAGE_SIZE_PARAMETER_NAME = "pageSize";
    
    /** "SELECT"参数名称 */
    private static final String SELECT_PARAMETER_NAME = "select";
    
    /** "FROM"参数名称 */
    private static final String FROM_PARAMETER_NAME = "from";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "recordPage";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
	    scope = new Scope(scope);
		Integer pageNumber = getParameter(PAGE_NUMBER_PARAMETER_NAME, Integer.class, scope);
		Integer pageSize = getParameter(PAGE_SIZE_PARAMETER_NAME, Integer.class, scope);
		String select = getParameter(SELECT_PARAMETER_NAME, String.class, scope);
		String from = getParameter(FROM_PARAMETER_NAME, String.class, scope);
		Page<Record> page = Db.paginate(pageNumber, pageSize, select,from);
        scope.setLocal(VARIABLE_NAME,page);
        stat.exec(env, scope, writer);
	}
	
    public boolean hasEnd() {
        return true;
    }	
}