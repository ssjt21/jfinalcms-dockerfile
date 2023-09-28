/*
 *
 *
 *
 */
package com.cms.template.directive;

import com.cms.TemplateVariable;
import com.cms.entity.Site;
import com.cms.entity.Tag;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.util.List;

/**
 * 模板指令 - 标签列表
 *
 *
 *
 */
@TemplateVariable(name="tag_list")
public class TagListDirective extends BaseDirective {

	/** 变量名称 */
	private static final String VARIABLE_NAME = "tags";


    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        // TODO Auto-generated method stub
        Site currentSite = getCurrentSite(scope);
        scope = new Scope(scope);
        Integer siteId = currentSite.getId();
        Integer start = getStart(scope);
        Integer count = getCount(scope);
        String condition = getCondition(scope);
        String orderBy = getOrderBy(scope);
        List<Tag> tags = new Tag().dao().findList(condition,orderBy, start,count,siteId);
        scope.setLocal(VARIABLE_NAME,tags);
        stat.exec(env, scope, writer);
    }

    public boolean hasEnd() {
        return true;
    }
}