package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;

import com.alibaba.fastjson.JSONArray;
import com.cms.Feedback;
import com.cms.entity.Menu;
import com.cms.entity.Role;
import com.cms.entity.RoleSite;
import com.cms.entity.Site;
import com.cms.routes.RouteMapping;


/**
 * Controller - 角色
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/role")
public class RoleController extends BaseController {
	
	/**
	 * 列表
	 */
	public void index() {
		setListQuery();
		Integer pageNumber = getParaToInt("pageNumber");
		if(pageNumber==null){
			pageNumber = 1;
		}
		setAttr("page", new Role().dao().findPage(pageNumber,PAGE_SIZE));
		render(getView("role/index"));
	}

	/**
	 * 添加
	 */
	public void add() {
		setAttr("sites", new Site().dao().findAll());
		setAttr("rootMenus", new Menu().dao().findRoots());
		render(getView("role/add"));
	}

	/**
	 * 保存
	 */
	public void save() {
		Role role =  getModel(Role.class,"",true); 
		String[] permissions = getParaValues("permissions");
        if(ArrayUtils.isNotEmpty(permissions)){
        	role.setPermission(JSONArray.toJSONString(permissions));
        }else{
        	role.setPermission("[]");
        }
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.save();
        Integer[] siteIds = getParaValuesToInt("siteIds");
        for(Integer siteId : siteIds){
        	RoleSite roleSite = new RoleSite();
        	roleSite.setRoleId(role.getId());
        	roleSite.setSiteId(siteId);
        	roleSite.save();
        }
		redirect(getListQuery("/admin/role"));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("role", new Role().dao().findById(id));
		setAttr("rootMenus", new Menu().dao().findRoots());
		setAttr("sites", new Site().dao().findAll());
		render(getView("role/edit"));
	}

	/**
	 * 修改
	 */
	public void update() {
		Role role =  getModel(Role.class,"",true); 
		String[] permissions = getParaValues("permissions");
        if(ArrayUtils.isNotEmpty(permissions)){
        	role.setPermission(JSONArray.toJSONString(permissions));
        }else{
        	role.setPermission("[]");
        }
        role.setUpdateDate(new Date());
        role.update();
        Integer[] siteIds = getParaValuesToInt("siteIds");
        new RoleSite().dao().deleteByRoleId(role.getId());
        for(Integer siteId : siteIds){
        	RoleSite roleSite = new RoleSite();
        	roleSite.setRoleId(role.getId());
        	roleSite.setSiteId(siteId);
        	roleSite.save();
        }
		redirect(getListQuery("/admin/role"));
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long ids[] = getParaValuesToLong("ids");
		if(ArrayUtils.isNotEmpty(ids)){
			for(Long id:ids){
				new Role().dao().deleteById(id);
			}
		}
		renderJson(Feedback.success(new HashMap<>()));
	}

}