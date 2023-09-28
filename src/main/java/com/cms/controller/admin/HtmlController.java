/*
 *
 *
 *
 */
package com.cms.controller.admin;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cms.Config;
import com.cms.Feedback;
import com.cms.util.SystemUtils;
import org.apache.commons.lang.time.DateUtils;

import com.cms.entity.Category;
import com.cms.entity.Content;
import com.cms.routes.RouteMapping;
import com.cms.util.CacheUtils;
import com.cms.util.HtmlUtils;

/**
 * Controller - 静态化
 *
 *
 *
 */
@RouteMapping(url = "/admin/html")
public class HtmlController extends BaseController {

	/**
	 * 生成静态
	 */
	public void index(){
		setAttr("defaultStartDate", DateUtils.addDays(new Date(), -7));
		setAttr("defaultEndDate", new Date());
		setAttr("categoryTree", new Category().dao().findTree(null,getCurrentSite().getId()));
		render(getView("html/index"));
	}

	/**
	 * 生成静态
	 */
	public void generate() {
		Config config = SystemUtils.getConfig();
		if(config.getSiteModel()!=Config.SiteModel.HTML.ordinal()){
			renderJson(Feedback.error("网站模式不是静态模式"));
			return;
		}
		Integer type = getParaToInt("type");
		Integer categoryId = getParaToInt("categoryId");
		Date startDate = getParaToDate("startDate");
		Date endDate = getParaToDate("endDate");
		Integer first = getParaToInt("first");
		Integer count = getParaToInt("count");
		long startTime = System.currentTimeMillis();
		if (startDate != null) {
			Calendar calendar = DateUtils.toCalendar(startDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			startDate = calendar.getTime();
		}
		if (endDate != null) {
			Calendar calendar = DateUtils.toCalendar(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endDate = calendar.getTime();
		}
		if (first == null || first < 0) {
			first = 0;
			CacheUtils.clearAll();
		}
		if (count == null || count <= 0) {
			count = 100;
		}
		int generateCount = 0;
		boolean isCompleted = true;
		if(type==HtmlUtils.Type.INDEX.ordinal()){
			generateCount = HtmlUtils.generateIndex(getCurrentSite().getId());
		}else if(type==HtmlUtils.Type.CONTENT.ordinal()){
			List<Content> contents = new Content().dao().findList(categoryId,null, startDate, endDate,null, first, count,getCurrentSite().getId());
			for (Content content : contents) {
				generateCount += HtmlUtils.generate(content);
			}
			first += contents.size();
			if (contents.size() == count) {
				isCompleted = false;
			}
		}else if(type==HtmlUtils.Type.CATEGORY.ordinal()){
			if(categoryId == null){
				List<Category> categoryTree = new Category().dao().findTree(null,getCurrentSite().getId());
				for(Category category : categoryTree){
					generateCount += HtmlUtils.generate(category);
				}
			}else{
				Category category = new Category().dao().findById(categoryId);
				generateCount += HtmlUtils.generate(category);
			}
		}else{
			generateCount += HtmlUtils.generateAll(getCurrentSite().getId());
		}
		long endTime = System.currentTimeMillis();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("first", first);
		data.put("generateCount", generateCount);
		data.put("generateTime", endTime - startTime);
		data.put("isCompleted", isCompleted);
		renderJson(Feedback.success(data));
	}

}