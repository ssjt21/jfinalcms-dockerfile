package com.cms.controller.xcx;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.cms.Feedback;
import com.cms.controller.front.BaseController;
import com.cms.entity.Content;
import com.cms.routes.RouteMapping;

/**
 * Controller - 内容
 * 
 * 
 * 
 */
@RouteMapping(url = "/xcx/content")
public class ContentController extends BaseController {

	/**
	 * 内容页
	 */
	public void index(){
		Long contentId = getParaToLong("contentId");
		Content content = new Content().dao().findById(contentId);
		Map<String,Object> map = new HashMap<>();
		map.put("title", content.getTitle());
		map.put("createDate", DateFormatUtils.format(content.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		map.put("ico",content.getIco());
		map.put("introduction",content.getIntroduction());
		map.put("text", StringUtils.substring(content.getText(), 0,50));
		Map<String,Object> result = new HashMap<>();
		result.put("content", map);
		renderJson(Feedback.success(result));
		
	}
}
