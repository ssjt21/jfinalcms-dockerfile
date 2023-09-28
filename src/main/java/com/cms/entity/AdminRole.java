package com.cms.entity;

import com.cms.entity.base.BaseAdminRole;
import com.jfinal.plugin.activerecord.Db;

/**
 * Entity - 管理员角色
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class AdminRole extends BaseAdminRole<AdminRole> {
	
	/**
	 * 根据管理员ID删除管理员角色
	 * 
	 * @param adminId
	 */
	public void deleteByAdminId(Integer adminId){
		Db.update("delete from cms_admin_role where adminId=?",adminId);
	}
	
}
