package com.cms.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cms.Feedback;
import com.cms.entity.WechatMenu;
import com.cms.routes.RouteMapping;
import com.cms.util.WeixinUtils;
import com.jfinal.core.NotAction;
/**
 * Controller - 微信菜单
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/wechat_menu")
public class WechatMenuController extends BaseController {

	/**
	 * 编辑
	 */
	public void index() {
		List<WechatMenu> wechatMenus = new WechatMenu().dao().findRoots();
		setAttr("wechatMenus", wechatMenus);
		render(getView("wechat_menu/index"));
	}
	
	/**
	 * 修改
	 */
	public void update() {
		String data = getPara("data");
		JSONArray jsonArray = JSONArray.parseArray(data);
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			WechatMenu wechatMenu = updateData(jsonObject,null);
			JSONArray childArray = jsonObject.getJSONArray("child");
			if(childArray.size()>0){
				for(int j=0;j<childArray.size();j++){
					JSONObject childObject = childArray.getJSONObject(j);
					updateData(childObject,wechatMenu.getId());
				}
			}
		}
		List<WechatMenu> wechatMenus = new WechatMenu().dao().findRoots();
		List<Map<String,Object>> button = new ArrayList<>();
		for(WechatMenu wechatMenu : wechatMenus){
			List<WechatMenu> subMenus = wechatMenu.getSubMenus();
			if(CollectionUtils.isNotEmpty(subMenus)){
				Map<String,Object> map = new HashMap<>();
				map.put("name", wechatMenu.getName());
				List<Map<String,Object>> list = new ArrayList<>();
				for(WechatMenu subMenu : subMenus){
					Map<String,Object> subMap = new HashMap<>();
					subMap.put("type", subMenu.getType());
					subMap.put("name", subMenu.getName());
					if("click".equals(subMenu.getType())){
						subMap.put("key", subMenu.getKeywords());
					}else if("view".equals(subMenu.getType())){
						subMap.put("url", subMenu.getUrl());
					}else if("miniprogram".equals(subMenu.getType())){
						subMap.put("appid", subMenu.getMiniappid());
						subMap.put("url", subMenu.getMiniurl());
						subMap.put("pagepath", subMenu.getMinipath());
					}
					list.add(subMap);
				}
				map.put("sub_button", list);
				button.add(map);
			}else{
				Map<String,Object> map = new HashMap<>();
				map.put("name", wechatMenu.getName());
				map.put("type", wechatMenu.getType());
				if("click".equals(wechatMenu.getType())){
					map.put("key", wechatMenu.getKeywords());
				}else if("view".equals(wechatMenu.getType())){
					map.put("url", wechatMenu.getUrl());
				}else if("miniprogram".equals(wechatMenu.getType())){
					map.put("appid", wechatMenu.getMiniappid());
					map.put("url", wechatMenu.getMiniurl());
					map.put("pagepath", wechatMenu.getMinipath());
				}
				button.add(map);
			}
		}
		Map<String,Object> menu = new HashMap<>();
		menu.put("button", button);
		WeixinUtils.menuCreate(JSONObject.toJSONString(menu));
		renderJson(Feedback.success(new HashMap<>()));
	}
	
	@NotAction
	public WechatMenu updateData(JSONObject jsonObject,Integer parentId){
		Long id = jsonObject.getLong("id");
		String name = jsonObject.getString("name");
		String type = jsonObject.getString("type");
		String keywords = jsonObject.getString("keywords");
		String url = jsonObject.getString("url");
		String miniappid = jsonObject.getString("miniappid");
		String miniurl = jsonObject.getString("miniurl");
		String minipath = jsonObject.getString("minipath");
		WechatMenu wechatMenu = null;
		if(id != null){
			wechatMenu = new WechatMenu().dao().findById(id);
		}
		if(wechatMenu == null){
			wechatMenu = new WechatMenu();
			wechatMenu.setCreateDate(new Date());
		}
		wechatMenu.setUpdateDate(new Date());
		wechatMenu.setParentId(parentId);
		wechatMenu.setName(name);
		wechatMenu.setType(type);
		wechatMenu.setKeywords(keywords);
		wechatMenu.setUrl(url);
		wechatMenu.setMiniappid(miniappid);
		wechatMenu.setMiniurl(miniurl);
		wechatMenu.setMinipath(minipath);
		if(wechatMenu.getId() == null){
			wechatMenu.save();
		}else{
			wechatMenu.update();
		}
		return wechatMenu;
	}
}
