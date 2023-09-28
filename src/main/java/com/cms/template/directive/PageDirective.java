/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import java.util.ArrayList;
import java.util.List;

import com.cms.TemplateVariable;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 模板指令 - 分页
 * 
 * 
 * 
 */
@TemplateVariable(name="page")
public class PageDirective extends BaseDirective {

	/** "模式"参数名称 */
	private static final String PATTERN_PARAMETER_NAME = "pattern";

	/** "页码"参数名称 */
	private static final String PAGE_NUMBER_PARAMETER_NAME = "pageNumber";

	/** "总页数"参数名称 */
	private static final String TOTAL_PAGES_PARAMETER_NAME = "totalPages";

	/** "中间段页码数"参数名称 */
	private static final String SEGMENT_COUNT_PARAMETER_NAME = "segmentCount";

	/** "模式"变量名称 */
	private static final String PATTERN_VARIABLE_NAME = "pattern";

	/** "页码"变量名称 */
	private static final String PAGE_NUMBER_VARIABLE_NAME = "pageNumber";

	/** "总页数"变量名称 */
	private static final String PAGE_COUNT_VARIABLE_NAME = "totalPages";

	/** "中间段页码数"变量名称 */
	private static final String SEGMENT_COUNT_VARIABLE_NAME = "segmentCount";

	/** "是否存在上一页"变量名称 */
	private static final String HAS_PREVIOUS_VARIABLE_NAME = "hasPrevious";

	/** "是否存在下一页"变量名称 */
	private static final String HAS_NEXT_VARIABLE_NAME = "hasNext";

	/** "是否为首页"变量名称 */
	private static final String IS_FIRST_VARIABLE_NAME = "isFirst";

	/** "是否为末页"变量名称 */
	private static final String IS_LAST_VARIABLE_NAME = "isLast";

	/** "上一页页码"变量名称 */
	private static final String PREVIOUS_PAGE_NUMBER_VARIABLE_NAME = "previousPageNumber";

	/** "下一页页码"变量名称 */
	private static final String NEXT_PAGE_NUMBER_VARIABLE_NAME = "nextPageNumber";

	/** "首页页码"变量名称 */
	private static final String FIRST_PAGE_NUMBER_VARIABLE_NAME = "firstPageNumber";

	/** "末页页码"变量名称 */
	private static final String LAST_PAGE_NUMBER_VARIABLE_NAME = "lastPageNumber";

	/** "中间段页码"变量名称 */
	private static final String SEGMENT_VARIABLE_NAME = "segment";

	@Override
    public void exec(Env env, Scope scope, Writer writer) {
	    scope = new Scope(scope);
		String pattern = getParameter(PATTERN_PARAMETER_NAME, String.class, scope);
		Integer pageNumber = getParameter(PAGE_NUMBER_PARAMETER_NAME, Integer.class, scope);
		Integer totalPages = getParameter(TOTAL_PAGES_PARAMETER_NAME, Integer.class, scope);
		Integer segmentCount = getParameter(SEGMENT_COUNT_PARAMETER_NAME, Integer.class, scope);

		if (pageNumber == null || pageNumber < 1) {
			pageNumber = 1;
		}
		if (totalPages == null || totalPages < 1) {
			totalPages = 1;
		}
		if (segmentCount == null || segmentCount < 1) {
			segmentCount = 5;
		}
		boolean hasPrevious = pageNumber > 1;
		boolean hasNext = pageNumber < totalPages;
		boolean isFirst = pageNumber == 1;
		boolean isLast = pageNumber.equals(totalPages);
		int previousPageNumber = pageNumber - 1;
		int nextPageNumber = pageNumber + 1;
		int firstPageNumber = 1;
		int lastPageNumber = totalPages;
		int startSegmentPageNumber = pageNumber - (int) Math.floor((segmentCount - 1) / 2D);
		int endSegmentPageNumber = pageNumber + (int) Math.ceil((segmentCount - 1) / 2D);
		if (startSegmentPageNumber < 1) {
			endSegmentPageNumber = segmentCount;
		}
		if (endSegmentPageNumber > totalPages) {
			startSegmentPageNumber = totalPages - (segmentCount - 1);
		}
		if (startSegmentPageNumber < 1) {
            startSegmentPageNumber = 1;
        }
        if (endSegmentPageNumber > totalPages) {
            endSegmentPageNumber = totalPages;
        }
		List<Integer> segment = new ArrayList<Integer>();
		for (int i = startSegmentPageNumber; i <= endSegmentPageNumber; i++) {
			segment.add(i);
		}

		scope.setLocal(PATTERN_VARIABLE_NAME, pattern);
		scope.setLocal(PAGE_NUMBER_VARIABLE_NAME, pageNumber);
		scope.setLocal(PAGE_COUNT_VARIABLE_NAME, totalPages);
		scope.setLocal(SEGMENT_COUNT_VARIABLE_NAME, segmentCount);
		scope.setLocal(HAS_PREVIOUS_VARIABLE_NAME, hasPrevious);
		scope.setLocal(HAS_NEXT_VARIABLE_NAME, hasNext);
		scope.setLocal(IS_FIRST_VARIABLE_NAME, isFirst);
		scope.setLocal(IS_LAST_VARIABLE_NAME, isLast);
		scope.setLocal(PREVIOUS_PAGE_NUMBER_VARIABLE_NAME, previousPageNumber);
		scope.setLocal(NEXT_PAGE_NUMBER_VARIABLE_NAME, nextPageNumber);
		scope.setLocal(FIRST_PAGE_NUMBER_VARIABLE_NAME, firstPageNumber);
		scope.setLocal(LAST_PAGE_NUMBER_VARIABLE_NAME, lastPageNumber);
		scope.setLocal(SEGMENT_VARIABLE_NAME, segment);
        stat.exec(env, scope, writer);
	}
	
    public boolean hasEnd() {
        return true;
    }
}