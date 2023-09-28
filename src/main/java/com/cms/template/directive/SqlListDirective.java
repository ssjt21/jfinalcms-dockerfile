package com.cms.template.directive;

import java.util.List;

import com.cms.TemplateVariable;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
/**
 * 模板指令 - SQL列表
 * 
 * 
 * 
 */
@TemplateVariable(name="sql_list")
public class SqlListDirective extends BaseDirective {

    /** "SQL"参数名称 */
    private static final String SQL_PARAMETER_NAME = "sql";
    
    /** 变量名称 */
    private static final String VARIABLE_NAME = "records";
    
    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        scope = new Scope(scope);
        String sql = getParameter(SQL_PARAMETER_NAME, String.class, scope);
        List<Record> records = Db.find(sql);
        scope.setLocal(VARIABLE_NAME,records);
        stat.exec(env, scope, writer);
    }
    
    public boolean hasEnd() {
        return true;
    }   
}
