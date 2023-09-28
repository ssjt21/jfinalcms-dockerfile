/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import java.util.List;

import com.cms.TemplateVariable;
import com.cms.entity.Site;
import com.cms.entity.Slide;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 模板指令 - 幻灯片列表
 * 
 * 
 * 
 */
@TemplateVariable(name="slide_list")
public class SlideListDirective extends BaseDirective {
	
	/** "分组ID"参数名称 */
	private static final String GID_PARAMETER_NAME = "gid";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "slides";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
	    Site currentSite = getCurrentSite(scope);
	    scope = new Scope(scope);
	    Integer siteId = currentSite.getId();
	    Integer gid = getParameter(GID_PARAMETER_NAME, Integer.class, scope);
	    Integer start = getStart(scope);
	    Integer count = getCount(scope);
		String orderBy = getOrderBy(scope);
		List<Slide> slides = new Slide().dao().findList(gid,orderBy,start,count,siteId);
		scope.setLocal(VARIABLE_NAME,slides);
        stat.exec(env, scope, writer);
	}
	
    public boolean hasEnd() {
        return true;
    }	
}