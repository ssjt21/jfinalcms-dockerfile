package com.cms.entity;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.cms.entity.base.BaseWechatMenu;
import com.cms.util.DbUtils;

/**
 * Entity - 微信菜单
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class WechatMenu extends BaseWechatMenu<WechatMenu> {
	
	/**
	 * 下级微信菜单
	 */
	@JSONField(serialize=false)  
	private List<WechatMenu> subMenus;
	
	
	/**
	 * 获取下级微信菜单
	 * 
	 * @return 下级微信菜单
	 */
	public List<WechatMenu> getSubMenus() {
	    if(subMenus == null){
	    	subMenus = find("select * from cms_wechat_menu where parentId=? order by sort asc",getId());
	    }
		return subMenus;
	}
	
	/**
	 * 查找顶级微信菜单
	 * 
	 * @return 顶级微信菜单
	 */
	public List<WechatMenu> findRoots(){
	    String filterSql = "";
		String orderBySql = DbUtils.getOrderBySql("sort asc");
		return find("select * from cms_wechat_menu where parentId is null"+filterSql+orderBySql);
	}
}
