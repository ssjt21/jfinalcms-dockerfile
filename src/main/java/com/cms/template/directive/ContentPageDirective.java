/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import com.cms.TemplateVariable;
import com.cms.entity.Content;
import com.cms.entity.Site;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 模板指令 - 内容分页
 * 
 * 
 * 
 */
@TemplateVariable(name="content_page")
public class ContentPageDirective extends BaseDirective {

	/** "栏目ID"参数名称 */
	private static final String CATEGORY_ID_PARAMETER_NAME = "categoryId";

	/** "标签ID"参数名称 */
	private static final String TAG_ID_PARAMETER_NAME = "tagId";
	
    /** "搜索词"参数名称 */
    private static final String KEYWORD_PARAMETER_NAME = "keyword";
    
    /** "页码"参数名称 */
    private static final String PAGE_NUMBER_PARAMETER_NAME = "pageNumber";
    
    /** "每页记录数"参数名称 */
    private static final String PAGE_SIZE_PARAMETER_NAME = "pageSize";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "contentPage";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
	    Site currentSite = getCurrentSite(scope);
	    scope = new Scope(scope);
	    Integer siteId = currentSite.getId();
		Integer categoryId = getParameter(CATEGORY_ID_PARAMETER_NAME, Integer.class, scope);
		Integer tagId = getParameter(TAG_ID_PARAMETER_NAME, Integer.class, scope);
		String keyword = getParameter(KEYWORD_PARAMETER_NAME, String.class, scope);
		Integer pageNumber = getParameter(PAGE_NUMBER_PARAMETER_NAME, Integer.class, scope);
		Integer pageSize = getParameter(PAGE_SIZE_PARAMETER_NAME, Integer.class, scope);
		String condition = getCondition(scope);
		String orderBy = getOrderBy(scope);
		Page<Content> page = new Content().dao().findPage(categoryId,tagId,keyword,condition, orderBy,pageNumber,pageSize,siteId);
		scope.setLocal(VARIABLE_NAME,page);
        stat.exec(env, scope, writer);
        try{
			writer.write("<!--totalPageStart("+page.getTotalPage()+")totalPageEnd-->");
		}catch (Exception e){
        	e.printStackTrace();
		}
	}
	
    public boolean hasEnd() {
        return true;
    }	
}