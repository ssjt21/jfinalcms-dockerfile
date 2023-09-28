/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.cms.CommonAttribute;
import com.cms.Feedback;
import com.cms.entity.Admin;
import com.cms.entity.AdminRole;
import com.cms.entity.Role;
import com.cms.routes.RouteMapping;


/**
 * Controller - 管理员
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/admin")

public class AdminController extends BaseController {
	
	/**
	 * 列表
	 */
	public void index(){
		setListQuery();
	    String name = getPara("name");
	    String username = getPara("username");
		Integer pageNumber = getParaToInt("pageNumber");
		if(pageNumber==null){
			pageNumber = 1;
		}
		setAttr("page", new Admin().dao().findPage(name,username,pageNumber,PAGE_SIZE));
		setAttr("name", name);
		setAttr("username", username);
		render(getView("admin/index"));
	}

	/**
	 * 检查用户名是否存在
	 */
	public void checkUsername() {
		String username = getPara("username");
		if (StringUtils.isEmpty(username)) {
			renderJson(false);
			return;
		}
		renderJson(!new Admin().dao().usernameExists(username));
	}

	/**
	 * 添加
	 */
	public void add() {
		setAttr("roles", new Role().dao().findAll());
		render(getView("admin/add"));
	}

	/**
	 * 保存
	 */
	public void save() {
		Admin admin =  getModel(Admin.class,"",true); 
		if (new Admin().dao().usernameExists(admin.getUsername())) {
			render(CommonAttribute.ADMIN_ERROR_VIEW);
			return;
		}
		admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
		admin.setCreateDate(new Date());
		admin.setUpdateDate(new Date());
		admin.save();
		Integer[] roleIds = getParaValuesToInt("roleIds");
        for(Integer roleId : roleIds){
        	AdminRole adminRole = new AdminRole();
        	adminRole.setAdminId(admin.getId());
        	adminRole.setRoleId(roleId);
        	adminRole.save();
        }
		redirect(getListQuery("/admin/admin"));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Integer id = getParaToInt("id");
		setAttr("admin", new Admin().dao().findById(id));
		setAttr("roles", new Role().dao().findAll());
		render(getView("admin/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		Admin admin =  getModel(Admin.class,"",true); 
		Admin pAdmin = new Admin().dao().findById(admin.getId());
		if (pAdmin == null) {
			render(CommonAttribute.ADMIN_ERROR_VIEW);
			return;
		}
		if (StringUtils.isNotEmpty(admin.getPassword())) {
			admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
		} else {
			admin.setPassword(pAdmin.getPassword());
		}
		admin.setUpdateDate(new Date());
		admin.update();
		Integer[] roleIds = getParaValuesToInt("roleIds");
        new AdminRole().dao().deleteByAdminId(admin.getId());
        for(Integer roleId : roleIds){
        	AdminRole adminRole = new AdminRole();
        	adminRole.setAdminId(admin.getId());
        	adminRole.setRoleId(roleId);
        	adminRole.save();
        }
		redirect(getListQuery("/admin/admin"));
	}
	
	/**
	 * 重置密码
	 */
	public void reset(){
		Integer id = getParaToInt("id");
		Admin admin = new Admin().dao().findById(id);
		admin.setPassword(DigestUtils.md5Hex("123456"));
		admin.setUpdateDate(new Date());
		admin.update();
		renderJson(Feedback.success(new HashMap<>()));
	}

	/**
	 * 删除
	 */
	public void delete() {
		Integer ids[] = getParaValuesToInt("ids");
		if(ArrayUtils.isNotEmpty(ids)){
			for(Integer id:ids){
				new Admin().dao().deleteById(id);
			}
		}
		renderJson(Feedback.success(new HashMap<>()));
	}

}