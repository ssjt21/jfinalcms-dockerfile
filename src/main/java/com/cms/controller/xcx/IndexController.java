package com.cms.controller.xcx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.cms.Feedback;
import com.cms.controller.front.BaseController;
import com.cms.entity.Content;
import com.cms.entity.Slide;
import com.cms.routes.RouteMapping;
/**
 * Controller - api
 * 
 * 
 * 
 */
@RouteMapping(url = "/xcx")
public class IndexController extends BaseController {
	
	/**
	 * 首页
	 */
	public void index() {
		Long siteId = getParaToLong("siteId");
		Long categoryId = getParaToLong("categoryId");
		List<Slide> slides = new Slide().dao().find("select * from cms_slide where siteId="+siteId);
		List<Map<String,Object>> slidesMap = new ArrayList<>();
		for(Slide slide : slides){
			Map<String,Object> map = new HashMap<>();
			map.put("image", slide.getImage());
			map.put("url", slide.getUrl());
			slidesMap.add(map);
		}
		List<Content> contents = new Content().dao().find("select * from cms_content where siteId="+siteId+" and categoryId="+categoryId+" limit 4");
		List<Map<String,Object>> contentsMap = new ArrayList<>();
		for(Content content : contents){
			Map<String,Object> map = new HashMap<>();
			map.put("id", content.getId());
			map.put("title", content.getTitle());
			map.put("createDate", DateFormatUtils.format(content.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
			map.put("ico",content.getIco());
			map.put("text", StringUtils.substring(content.getText(), 0,50));
			contentsMap.add(map);
		}
		Map<String,Object> result = new HashMap<>();
		result.put("slides", slidesMap);
		result.put("contents", contentsMap);
		renderJson(Feedback.success(result));
	}
}
